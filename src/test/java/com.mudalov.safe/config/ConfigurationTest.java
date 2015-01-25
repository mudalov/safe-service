package com.mudalov.safe.config;

import org.junit.Assert;
import org.junit.Test;


public class ConfigurationTest {

    @Test
    public void testLoad() {
        Configuration configuration = Configuration.root();
        Assert.assertEquals(Integer.valueOf(2), configuration.getMaxWorkQueueSize());
        Assert.assertEquals(Integer.valueOf(2), configuration.getThreadsPerGroup());
    }

    @Test
    public void testGroupProperties() {
        Configuration rootConfig = Configuration.root();
        Assert.assertEquals(Integer.valueOf(2), rootConfig.getThreadsPerGroup());
        Configuration configuration = rootConfig.forGroup("userInfoService");
        Assert.assertEquals(Integer.valueOf(2), configuration.getMaxWorkQueueSize());
        Assert.assertEquals(Integer.valueOf(12), configuration.getThreadsPerGroup());
    }

}
