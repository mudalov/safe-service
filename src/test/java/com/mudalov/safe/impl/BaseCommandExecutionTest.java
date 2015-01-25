package com.mudalov.safe.impl;


import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseCommandExecutionTest {

    @Test
    public void testExecute() throws Exception {
        // simplest case
        AbstractCommand<Integer> sum = new AbstractCommand<Integer>() {
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
        AbstractCommand<Integer> slowSum = new AbstractCommand<Integer>() {
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

    @Test
    public void testExecute_FallBackOnExecutionException() {
        final AtomicInteger errorFlag = new AtomicInteger(0);
        AbstractCommand<Integer> errorSum = new AbstractCommand<Integer>() {
            @Override
            public Integer action() throws Exception {
                throw new IllegalStateException();
            }

            @Override
            public void onError(Exception cause) {
                Assert.assertTrue(cause instanceof ExecutionException);
                Assert.assertTrue(cause.getCause() instanceof IllegalStateException);
                errorFlag.incrementAndGet();
            }

            @Override
            public Integer fallback() {
                return 2;
            }
        };
        CommandRef<Integer> commandRef = SafeCommands.create(errorSum);
        Integer result = commandRef.execute();
        Assert.assertEquals(result, errorSum.fallback());
        Assert.assertEquals(1, errorFlag.get());
    }

    @Test
    public void testExecute_FallBackOnRejected() throws InterruptedException {
        final AtomicInteger errorFlag = new AtomicInteger(0);
        final AbstractCommand<Integer> sum = new AbstractCommand<Integer>() {
            @Override
            public Integer action() throws Exception {
                Thread.sleep(500);
                return 1;
            }

            @Override
            public void onError(Exception cause) {
                Assert.assertTrue(cause instanceof RejectedExecutionException);
                errorFlag.incrementAndGet();
            }
        };
        // emulation of several clients calling the same service
        ExecutorService clientExecutor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            clientExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    SafeCommands.create(sum, "toManyConnections").execute();
                }
            });
        }
        clientExecutor.awaitTermination(5, TimeUnit.SECONDS);
        Assert.assertTrue(errorFlag.get() > 0);
    }



}
