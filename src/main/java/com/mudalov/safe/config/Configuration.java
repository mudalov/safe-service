package com.mudalov.safe.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * User: mudalov
 * Date: 24/01/15
 * Time: 21:10
 */
public class Configuration {

    public static final String DefaultConfigFile = "safe-service.conf";

    private static class Props {
        final static String ThreadsNumber = "nThreads";
        final static String MaxWorkQueueSize = "maxWorkQueueSize";
    }

    private Configuration(){}

    private Config baseConfig;

    private Configuration(Config baseConfig) {
        this.baseConfig = baseConfig;
        // verify defaults provided
        this.baseConfig.getInt(Props.ThreadsNumber);
        this.baseConfig.getInt(Props.MaxWorkQueueSize);
    }

    public static Configuration load() {
        return load(DefaultConfigFile);
    }

    public static Configuration load(String location) {
        Config baseConfig = ConfigFactory.load(location);
        Configuration configuration = new Configuration(baseConfig);
        return configuration;
    }



}
