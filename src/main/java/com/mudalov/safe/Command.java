package com.mudalov.safe;

/**
 * User: mudalov
 * Date: 22/01/15
 * Time: 23:35
 */
public interface Command<T> {

    /**
     * Action to execute, called asynchronously in separate thread
     *
     */
    public abstract T action();

    /**
     * Result in case of task failure, called from client thread
     *
     * @return
     */
    public T fallback();
}
