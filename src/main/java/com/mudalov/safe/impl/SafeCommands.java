package com.mudalov.safe.impl;

import com.mudalov.safe.Command;
import com.mudalov.safe.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: mudalov
 * Date: 22/01/15
 * Time: 23:57
 */
public class SafeCommands {

    private static final Logger log = LoggerFactory.getLogger(SafeCommands.class);

    private static final ReentrantLock groupContextLock = new ReentrantLock();

    private static final Map<String, GroupExecutionContext> groupContexts = new ConcurrentHashMap<String, GroupExecutionContext>();

    private String group = "DefaultGroup";

    private SafeCommands() {
    }

    public static <T> Command<T> create(Class<BaseCommand<T>> commandClass, String group) {
        GroupExecutionContext groupContext = getGroupContext(group);
        BaseCommand<T> command = ReflectionUtils.createInstanceSilently(commandClass);
        command.setContext(groupContext);
        return command;
    }

    private static GroupExecutionContext getGroupContext(String groupName) {
        GroupExecutionContext context = groupContexts.get(groupName);
        if (context != null) {
            return context;
        }
        groupContextLock.lock();
        try {
            context = groupContexts.get(groupName);
            if (context != null) {
                return context;
            }
            context = new GroupExecutionContext(groupName);
            groupContexts.put(groupName, context);
            return context;
        } finally {
            groupContextLock.unlock();
        }
    }

}
