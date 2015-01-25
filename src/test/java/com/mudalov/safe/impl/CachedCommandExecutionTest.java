package com.mudalov.safe.impl;


import com.mudalov.safe.cache.TestCacheFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class CachedCommandExecutionTest {

    @Test
    public void testExecute() {
        AbstractCachedCommand<Date> getDate = new AbstractCachedCommand<Date>() {
            @Override
            public String getKey() {
                return "sum";
            }

            @Override
            public Date action() throws Exception {
                return new Date();
            }
        };
        // success, cache updated
        Date result = SafeCommands.create(getDate).execute();
        AbstractCachedCommand<Date> failingCommand = new AbstractCachedCommand<Date>() {
            @Override
            public String getKey() {
                return "sum";
            }

            @Override
            public Date action() throws Exception {
                throw new Exception();
            }
        };
        Date failedServiceResult = SafeCommands.create(failingCommand).execute();
        Assert.assertEquals("Expect cached result", result, failedServiceResult);
    }

    @Test
    public void testCustomCache() {
        AbstractCachedCommand<String> helloWorld = new AbstractCachedCommand<String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String action() throws Exception {
                return "Hello, world!";
            }
        };
        // success, cache updated
        SafeCommands.create(helloWorld, "customCache").execute();
        AbstractCachedCommand<String> failingCommand = new AbstractCachedCommand<String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String action() throws Exception {
                throw new Exception();
            }
        };
        String failedServiceResult = SafeCommands.create(failingCommand, "customCache").execute();
        Assert.assertEquals("Expect result from Test Cache", TestCacheFactory.CacheValue, failedServiceResult);
    }

    @Test
    public void testSeparateCacheRegions() {
        AbstractCachedCommand<String> helloWorld = new AbstractCachedCommand<String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String action() throws Exception {
                return "Hello, world!";
            }
        };
        AbstractCachedCommand<String> helloWorld2 = new AbstractCachedCommand<String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String action() throws Exception {
                return "Hello, world!!";
            }
        };
        // success, cache updated
        SafeCommands.create(helloWorld, "group1").execute();
        SafeCommands.create(helloWorld2, "group2").execute();
        AbstractCachedCommand<String> failingCommand = new AbstractCachedCommand<String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String action() throws Exception {
                throw new Exception();
            }
        };
        AbstractCachedCommand<String> failingCommand2 = new AbstractCachedCommand<String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String action() throws Exception {
                throw new Exception();
            }
        };
        String failedServiceResult = SafeCommands.create(failingCommand, "group1").execute();
        Assert.assertEquals("Hello, world!", failedServiceResult);
        failedServiceResult = SafeCommands.create(failingCommand2, "group2").execute();
        Assert.assertEquals("Hello, world!!", failedServiceResult);

    }


}
