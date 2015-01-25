package com.mudalov.safe.impl;

import com.mudalov.safe.CachedCommand;

/**
 * Building block for commands with cached results as a fallback values
 *
 * User: mudalov
 * Date: 25/01/15
 * Time: 22:45
 */
public abstract class AbstractCachedCommand<T> extends AbstractCommand<T> implements CachedCommand<T> {

    @Override
    public T fallback() {
        return getContext().getCachedValue(this);
    }
}
