package com.mudalov.safe.cache.ehcache;

import com.mudalov.safe.cache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * Default EhCache based cache implementation
 *
 * User: mudalov
 * Date: 24/01/15
 * Time: 23:42
 */
public class EhCacheImpl implements Cache {

    private final Ehcache ehcache;

    protected EhCacheImpl(Ehcache ehcache) {
        this.ehcache = ehcache;
    }

    @Override
    public <T> T get(String key) {
        Element element = ehcache.get(key);
        return element != null ? (T) element.getObjectValue() : null;
    }

    @Override
    public <T> void set(String key, T value) {
        ehcache.put(new Element(key, value));
    }
}
