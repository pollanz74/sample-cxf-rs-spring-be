package org.pollanz.samples.api.gateway.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableCaching
public class CacheConfig {

    @Autowired
    private Environment env;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        GuavaCache userAuthoritiesCache = new GuavaCache("userAuthorities", CacheBuilder.newBuilder()
//                .expireAfterWrite(env.getProperty("sample.gateway.cache.userAuthorities.expireAfter", Long.class, 1800L), TimeUnit.SECONDS)
//                .build());
//        GuavaCache validatedTokensCache = new GuavaCache("validatedTokens", CacheBuilder.newBuilder()
//                .expireAfterWrite(env.getProperty("sample.gateway.cache.validatedTokens.expireAfter", Long.class, 60L), TimeUnit.SECONDS)
//                .build());
//        cacheManager.setCaches(Arrays.asList(userAuthoritiesCache, validatedTokensCache));
        return cacheManager;
    }

}
