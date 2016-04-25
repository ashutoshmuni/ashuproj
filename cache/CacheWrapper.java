package com.citi.rulefabricator.cache;

/**
 * The Interface CacheWrapper.
 * 
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public interface CacheWrapper<K, V> {

    /**
     * Put.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void put(K key, V value);

    /**
     * Gets the.
     * 
     * @param key
     *            the key
     * @return the v
     */
    V get(K key);

    /**
     * Clear cache.
     * 
     */
    void clearCache();
}