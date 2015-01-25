package com.mudalov.safe.impl;


import org.junit.Assert;
import org.junit.Test;

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
//        BaseCommand<Integer>
    }


}
