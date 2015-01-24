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
        Assert.assertEquals(Integer.valueOf(10), configuration.getThreadsPerGroup());
    }

    @Test
    public void testCustomLocation() {
        Configuration configuration = Configuration.load("custom-safe-service");
        Assert.assertEquals(Integer.valueOf(15), configuration.getThreadsPerGroup());
    }

    @Test(expected = ConfigException.class)
    public void testLoad_FailOnMissedFile() {
        Configuration.load("does-not-exist");
    }

    @Test
    public void testGroupProperties() {
        Configuration configuration = Configuration.load().forGroup("userInfoService");
        Assert.assertEquals(Integer.valueOf(-1), configuration.getMaxWorkQueueSize());
        Assert.assertEquals(Integer.valueOf(12), configuration.getThreadsPerGroup());
    }

}
