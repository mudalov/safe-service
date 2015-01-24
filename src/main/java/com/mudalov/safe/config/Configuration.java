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

    public static final String DefaultConfigFile = "safe-service";

    private static Configuration rootConfig = load();

    /**
     * Supported configuration properties and their default values
     */
    private static class Props {
        final static Pair<String, Integer> ThreadsPerGroup = new Pair<String, Integer>("threadsPerGroup", 5);
        final static Pair<String, Integer> MaxWorkQueueSize = new Pair<String, Integer>("maxWorkQueueSize", -1);
        final static Pair<String, String> CacheFactory = new Pair<String, String>("maxWorkQueueSize", EhCacheFactory.class.getName());
        final static Pair<String, String> CacheConfigLocation = new Pair<String, String>("cacheConfigLocation", "safe-service-ehcache.xml");
    }

    private Configuration(){}

    private Config baseConfig;

    private Configuration(Config baseConfig) {
        this.baseConfig = baseConfig;
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
        return new Configuration(this.baseConfig.getConfig(groupName));
    }

    private <T> T getValue(Pair<String, T> prop) {
        return this.baseConfig.hasPath(prop.getFirst())
                ? (T)this.baseConfig.getAnyRef(prop.getFirst()) : prop.getSecond();
    }

    public Integer getMaxWorkQueueSize() {
        return getValue(Props.MaxWorkQueueSize);
    }

    public static Configuration load() {
        return load(DefaultConfigFile);
    }

    private static Configuration load(String baseName) {
        Config baseConfig = ConfigFactory.load(baseName);
        Configuration configuration = new Configuration(baseConfig);
        return configuration;
    }

}
