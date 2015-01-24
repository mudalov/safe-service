package com.mudalov.safe.cache.ehcache;

import com.mudalov.safe.cache.Cache;
import com.mudalov.safe.cache.CacheFactory;
import com.mudalov.safe.config.Configuration;
import net.sf.ehcache.CacheManager;

/**
 * Default EhCache based Cache Factory, each Dependency Group is configured as
 * a separate Cache region. Custom configuration file can be provided with 'cacheConfigLocation' property
 *
 * User: mudalov
 * Date: 25/01/15
 * Time: 00:27
 */
public class EhCacheFactory implements CacheFactory {

    private static CacheManager cacheManager = CacheManager.newInstance(Configuration.root().getCacheConfigLocation());

    @Override
    public Cache getCache(String groupName) {
        return new EhCacheImpl(cacheManager.addCacheIfAbsent(groupName));
    }

    @Override
    public Cache getCache() {
        return new EhCacheImpl(cacheManager.addCacheIfAbsent(CacheManager.DEFAULT_NAME));
    }
}
