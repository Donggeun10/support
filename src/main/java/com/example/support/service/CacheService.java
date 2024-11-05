package com.example.support.service;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CacheService {

    private final CacheManager cacheManager;

    public boolean isCacheDataAvailable(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            return valueWrapper != null;
        }
        return false;
    }

    public Object getCachedData(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                return valueWrapper.get();
            }
        }
        return null;
    }
}