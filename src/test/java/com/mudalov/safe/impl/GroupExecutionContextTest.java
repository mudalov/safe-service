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
        Integer result = SafeCommands.create(sum).execute();
        Assert.assertEquals(result, Integer.valueOf(2));
    }
}
