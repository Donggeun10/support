package com.example.support.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.cache.Caching;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.event.EventType;
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfig {
    // hazelcast-local.yml 파일을 사용하면 AOT 컴파일 시에 오류가 발생합니다. Hazelcast 5.2.4 이후 버전은 GraalVM과 호환되지 않습니다. (2024.11.01 기준)

    public static final String APPS = "apps";
    public static final String MEMBER = "member";

    @Bean
    @Profile("test")
    public HazelcastInstance hazelcastInstanceLocal() {
        Config config = new Config();
        config.setClusterName("hazelcast-local-cluster");

        config.getMapConfig(APPS).setTimeToLiveSeconds(60);
        config.getMapConfig(MEMBER).setTimeToLiveSeconds(60);

        NetworkConfig networkConfig = config.getNetworkConfig();
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(true);

        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    @Profile("local")
    public CacheManager jCacheCacheManager() {

        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
            .newEventListenerConfiguration(new CacheEventLogger(), EventType.CREATED, EventType.UPDATED, EventType.EVICTED, EventType.EXPIRED)
            .unordered().asynchronous();

        Map<String, CacheConfiguration<?, ?>> cacheMap = new HashMap<>();
        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder
            .heap(3)
            .offheap(1, MemoryUnit.MB) //min value is 1MB
            ;

        ExpiryPolicy<Object, Object> expiryPolicy = createExpiryPolicy(Duration.ofMinutes(3), Duration.ofMinutes(1));

        CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
            .newCacheConfigurationBuilder(Object.class, Object.class, resourcePoolsBuilder)
            .withExpiry(expiryPolicy)
            .withService(cacheEventListenerConfiguration)
            .build();

        cacheMap.put(APPS, cacheConfiguration);
        cacheMap.put(MEMBER, cacheConfiguration);
        EhcacheCachingProvider ehcacheCachingProvider = (EhcacheCachingProvider) Caching.getCachingProvider(EhcacheCachingProvider.class.getName());

        DefaultConfiguration defaultConfiguration = new DefaultConfiguration(cacheMap, ehcacheCachingProvider.getDefaultClassLoader());
        javax.cache.CacheManager cacheManager = ehcacheCachingProvider.getCacheManager(ehcacheCachingProvider.getDefaultURI(), defaultConfiguration);

        return new JCacheCacheManager(cacheManager);
    }

    private static ExpiryPolicy<Object, Object> createExpiryPolicy(Duration timeToLive, Duration timeToIdle) {
        return ExpiryPolicyBuilder
            .expiry()
            .create(timeToLive)
            .access(timeToIdle)
            .build();
    }
}
