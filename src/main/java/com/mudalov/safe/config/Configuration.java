package com.mudalov.safe.config;

import com.mudalov.safe.util.Pair;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigResolveOptions;

/**
 * User: mudalov
 * Date: 24/01/15
 * Time: 21:10
 */
public class Configuration {

    public static final String DefaultConfigFile = "safe-service";

    private static class Props {
        final static Pair<String, Integer> ThreadsPerGroup = new Pair<String, Integer>("threadsPerGroup", 5);
        final static Pair<String, Integer> MaxWorkQueueSize = new Pair<String, Integer>("maxWorkQueueSize", -1);
    }

    private Configuration(){}

    private Config baseConfig;

    private Configuration(Config baseConfig) {
        this.baseConfig = baseConfig;
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

    public static Configuration load(String baseName) {
        Config baseConfig = ConfigFactory.load(baseName,
                ConfigParseOptions.defaults().setAllowMissing(false),
                ConfigResolveOptions.defaults());
        Configuration configuration = new Configuration(baseConfig);
        return configuration;
    }



}
