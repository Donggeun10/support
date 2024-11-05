package com.example.support.controller;

import com.example.support.service.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class CacheController {

    private final CacheService cacheService;

    @Operation(summary = "check-cache", security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping("/check-cache")
    public String checkCache(@RequestParam String cacheName, @RequestParam String key) {
        boolean isAvailable = cacheService.isCacheDataAvailable(cacheName, key);
        return isAvailable ? "Cache data is available" : "Cache data is not available";
    }

    @Operation(summary = "get-cache", security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping("/get-cache")
    public String getCache(@RequestParam String cacheName, @RequestParam String key) {
        Object cachedData = cacheService.getCachedData(cacheName, key);
        return cachedData != null ? "Cached data: " + cachedData : "No cached data found";
    }
}
