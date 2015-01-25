package com.mudalov.safe;


/**
 * If command implements this interface, then result will be cached (and constantly updated by positive calls)
 * and used as a fallback for further calls. Custom cache implementation can be configured for each Dependency Group.
 *
 * @see com.mudalov.safe.cache.Cache
 * @see com.mudalov.safe.cache.CacheFactory
 *
 */
public interface CachedCommand<T> extends Command<T> {

    public String getKey();

}
