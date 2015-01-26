package com.mudalov.safe.config;

import com.mudalov.safe.cache.ehcache.EhCacheFactory;
import com.mudalov.safe.util.Pair;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * User: mudalov
 * Date: 24/01/15
 * Time: 21:10
 */
public class Configuration {

    public static final String DEFAULT_CONFIG_FILE = "safe-service";

    private static Configuration rootConfig = load();

    /**
     * Supported configuration properties and their default values
     */
    private static class Props {
        final static Pair<String, Integer> ThreadsPerGroup = new Pair<String, Integer>("threadsPerGroup", 5);
        final static Pair<String, Integer> MaxWorkQueueSize = new Pair<String, Integer>("maxWorkQueueSize", -1);
        final static Pair<String, String> CacheFactory = new Pair<String, String>("cacheFactory", EhCacheFactory.class.getName());
        final static Pair<String, String> CacheConfigLocation = new Pair<String, String>("cacheConfigLocation", "/safe-service-ehcache.xml");
        final static Pair<String, Integer> TimeOut = new Pair<String, Integer>("timeOut", 1000);
    }

    private Configuration(){
        this(null, null);
    }

    private final Config baseConfig;

    private final String basePath;

    private Configuration(Config baseConfig) {
        this(baseConfig, null);
    }

    private Configuration(Config baseConfig, String basePath) {
        this.baseConfig = baseConfig;
        this.basePath = basePath != null ? basePath + "." : null;
    }

    public static Configuration root() {
        return rootConfig;
    }

    public String getCacheFactory() {
        return getValue(Props.CacheFactory);
    }

    public String getCacheConfigLocation() {
        return getValue(Props.CacheConfigLocation);
    }

    public Integer getThreadsPerGroup() {
        return getValue(Props.ThreadsPerGroup);
    }

    public Configuration forGroup(String groupName) {
        return new Configuration(this.baseConfig, groupName);
    }

    private <T> T getValue(Pair<String, T> prop) {
        String key = prop.getFirst();
        if (basePath != null
                && this.baseConfig.hasPath(basePath + key)) {
            return (T)this.baseConfig.getAnyRef(basePath + key);
        } else {
            return this.baseConfig.hasPath(key)
                    ? (T)this.baseConfig.getAnyRef(key) : prop.getSecond();
        }
    }

    public Integer getMaxWorkQueueSize() {
        return getValue(Props.MaxWorkQueueSize);
    }

    public Integer getTimeOut() {
        return getValue(Props.TimeOut);
    }

    public static Configuration load() {
        return load(DEFAULT_CONFIG_FILE);
    }

    private static Configuration load(String baseName) {
        Config baseConfig = ConfigFactory.load(baseName);
        Configuration configuration = new Configuration(baseConfig);
        return configuration;
    }

}
