package com.mudalov.safe.impl;


import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class GroupExecutionContextTest {

    @Test
    public void testExecute() throws Exception {
        // simplest case
        BaseCommand<Integer> sum = new BaseCommand<Integer>() {
            @Override
            public Integer action() {
                return 1 + 1;
            }
        };
        CommandRef<Integer> commandRef = SafeCommands.create(sum);
        Integer result = commandRef.execute();
        Assert.assertEquals(result, Integer.valueOf(2));
    }

    @Test
    public void testExecute_FallBackOnTimeOut() {
        final AtomicInteger errorFlag = new AtomicInteger(0);
        BaseCommand<Integer> slowSum = new BaseCommand<Integer>() {
            @Override
            public Integer action() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Assert.fail();
                }
                return 1;
            }

            @Override
            public void onError(Exception cause) {
                Assert.assertTrue(cause instanceof TimeoutException);
                errorFlag.incrementAndGet();
            }

            @Override
            public Integer fallback() {
                return 2;
            }
        };

        CommandRef<Integer> commandRef = SafeCommands.create(slowSum, "slowService");
        Integer result = commandRef.execute();
        Assert.assertEquals(result, slowSum.fallback());
        Assert.assertEquals(1, errorFlag.get());

    }


}
