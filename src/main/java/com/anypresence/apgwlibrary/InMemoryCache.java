package com.anypresence.apgwlibrary;


import android.util.LruCache;

class InMemoryCache extends LruCache<String, String> {
    private static final int DEFAULT_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public InMemoryCache(int maxSize) {
        super(maxSize);
    }

    public InMemoryCache() {
        super(DEFAULT_CACHE_SIZE);
    }
}
