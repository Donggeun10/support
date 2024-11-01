package com.example.support.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
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
import org.springframework.context.annotation.Profile;

@EnableCaching
@Configuration
public class CacheConfig {
    // hazelcast-local.yml 파일을 사용하면 AOT 컴파일 시에 오류가 발생합니다. Hazelcast 5.2.4 이후 버전은 GraalVM과 호환되지 않습니다. (2024.11.01 기준)
    @Bean
    @Profile("test")
    public HazelcastInstance hazelcastInstanceLocal() {
        Config config = new Config();
        config.setClusterName("hazelcast-local-cluster");

        NetworkConfig networkConfig = config.getNetworkConfig();
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(true);

        return Hazelcast.newHazelcastInstance(config);
    }

    private final javax.cache.configuration.Configuration<Object, Object> jCacheConfiguration;

    public CacheConfig() {

        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
            .newEventListenerConfiguration(new CacheEventLogger(), EventType.CREATED, EventType.UPDATED, EventType.EVICTED, EventType.EXPIRED)
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
    @Profile("local")
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> cm.createCache("apps", jCacheConfiguration);
    }
}
