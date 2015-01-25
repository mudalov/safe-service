package com.mudalov.safe.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * API Entry point, links commands and execution contexts
 *
 * User: mudalov
 * Date: 22/01/15
 * Time: 23:57
 */
public class SafeCommands {

    private static final Logger log = LoggerFactory.getLogger(SafeCommands.class);

    private static final ReentrantLock groupContextLock = new ReentrantLock();

    private static final Map<String, GroupExecutionContext> groupContexts = new ConcurrentHashMap<String, GroupExecutionContext>();

    private static final String DefaultGroup = "DefaultGroup";

    private SafeCommands() {
    }

    public static <T> CommandRef<T> create(AbstractCommand<T> command, String group) {
        GroupExecutionContext groupContext = getGroupContext(group);
        command.setContext(groupContext);
        return new CommandRef<T>(groupContext, command);
    }

    public static <T> CommandRef<T> create(AbstractCommand<T> command) {
        return create(command, DefaultGroup);
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
