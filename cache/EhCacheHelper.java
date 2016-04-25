package com.citi.rulefabricator.cache.impl;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.citi.rulefabricator.cache.CacheWrapper;

/**
 * The Class EhCacheHelper.
 * 
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public class EhCacheHelper<K, V> implements CacheWrapper<K, V> {

    /** The cache manager. */
    private CacheManager cacheManager;

    private String cacheName;

    /**
     * Gets the cache.
     * 
     * @param cacheName
     *            the cache name
     * @return the cache
     */
    public Ehcache getCache() {
        return cacheManager.getEhcache(cacheName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citi.rulefabricator.cache.CacheWrapper#put(java.lang.Object,
     * java.lang.Object, java.lang.String)
     */
    @Override
    public void put(final K key, final V value) {

        getCache().put(new Element(key, value));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citi.rulefabricator.cache.CacheWrapper#get(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public V get(K key) {

        Element element = getCache().get(key);
        if (element != null) {
            return (V) element.getObjectValue();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.citi.rulefabricator.cache.CacheWrapper#clearCache(java.lang.String)
     */
    @Override
    public void clearCache() {
        if (null != getCache()) {
            getCache().removeAll();
        }

    }

    /**
     * Gets the cache manager.
     * 
     * @return the cacheManager
     */
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * Sets the cache manager.
     * 
     * @param cacheManager
     *            the cacheManager to set
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * @return the cacheName
     */
    public String getCacheName() {
        return cacheName;
    }

    /**
     * @param cacheName
     *            the cacheName to set
     */
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }
}