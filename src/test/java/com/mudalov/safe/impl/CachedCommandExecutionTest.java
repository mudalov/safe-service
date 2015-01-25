package com.mudalov.safe.impl;


import junit.framework.Assert;
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

}
