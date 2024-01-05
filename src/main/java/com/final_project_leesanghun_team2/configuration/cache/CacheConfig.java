package com.final_project_leesanghun_team2.configuration.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		List<CaffeineCache> caches = Arrays.stream(CacheType.values())
				.map(cache -> new CaffeineCache(
						cache.getCacheName(),
						Caffeine.newBuilder()
								.expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.SECONDS) // 생성되거나 수정된 시간기준 일정시간 후 제거
								.maximumSize(cache.getMaximumSize()) // 캐시 최대 엔트리 수
								.recordStats() // Statics 적용
								.build()
				))
				.collect(Collectors.toList());

		cacheManager.setCaches(caches);
		return cacheManager;
	}
}