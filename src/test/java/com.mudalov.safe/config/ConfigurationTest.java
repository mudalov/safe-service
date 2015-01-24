package com.mudalov.safe.config;

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
        Configuration configuration = Configuration.root();
        // default
        Assert.assertEquals(Integer.valueOf(-1), configuration.getMaxWorkQueueSize());
        // overwritten
        Assert.assertEquals(Integer.valueOf(10), configuration.getThreadsPerGroup());
    }

    @Test
    public void testGroupProperties() {
        Configuration configuration = Configuration.root().forGroup("userInfoService");
        Assert.assertEquals(Integer.valueOf(-1), configuration.getMaxWorkQueueSize());
        Assert.assertEquals(Integer.valueOf(12), configuration.getThreadsPerGroup());
    }

}
