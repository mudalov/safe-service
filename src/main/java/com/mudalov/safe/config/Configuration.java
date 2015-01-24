package com.mudalov.safe.config;

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

    private static class Props {
        final static Pair<String, Integer> ThreadsNumber = new Pair<String, Integer>("nThreads", 5);
        final static Pair<String, Integer> MaxWorkQueueSize = new Pair<String, Integer>("maxWorkQueueSize", -1);
    }

    private Configuration(){}

    private Config baseConfig;

    private Configuration(Config baseConfig) {
        this.baseConfig = baseConfig;
    }

    public Integer getThreadsNumber() {
        return getValue(Props.ThreadsNumber);
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
        Config baseConfig = ConfigFactory.load(baseName);
        Configuration configuration = new Configuration(baseConfig);
        return configuration;
    }



}
