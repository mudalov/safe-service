package com.mudalov.safe.config;

import com.typesafe.config.ConfigException;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: mudalov
 * Date: 24/01/15
 * Time: 21:43
 */
public class ConfigurationTest {

    @Test
    public void testLoad() {
        Configuration configuration = Configuration.load();
        // default
        Assert.assertEquals(Integer.valueOf(-1), configuration.getMaxWorkQueueSize());
        // overwritten
        Assert.assertEquals(Integer.valueOf(10), configuration.getThreadsNumber());
    }

    @Test(expected = ConfigException.class)
    public void testLoad_FailOnMissedFile() {
        Configuration.load("does-not-exist.conf");
    }

}
