package com.example.support.configuration;

import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    private final javax.cache.configuration.Configuration<Object, Object> jCacheConfiguration;

    public CacheConfig() {

        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
            .newEventListenerConfiguration(new CacheEventLogger(), EventType.CREATED, EventType.UPDATED)
            .unordered().asynchronous();

        this.jCacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                                                                   ResourcePoolsBuilder.newResourcePoolsBuilder()
                                                                       .heap(2000, EntryUnit.ENTRIES)
                                                                       .offheap(10, MemoryUnit.MB))
                .withService(cacheEventListenerConfiguration)
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(15)))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(30)))

        );
    }


    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> cm.createCache("apps", jCacheConfiguration);
    }
}
