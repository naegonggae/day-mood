package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.TagPost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CacheEvictService {

	private final CacheManager cacheManager;

	@CacheEvict(cacheNames = "top5UsersWithMostPosts", allEntries = true)
	public void top5UsersWithMostPosts() {
		// "top5UsersWithMostPosts" 캐시의 모든 데이터를 삭제
	}

	@CacheEvict(cacheNames = "top5UsersWithMostFollowers", allEntries = true)
	public void top5UsersWithMostFollowers() {
		// "top5UsersWithMostFollowers" 캐시의 모든 데이터를 삭제
	}

	@CacheEvict(cacheNames = "evictTop5UserByCreatedAtDescCache", allEntries = true)
	public void evictTop5UserByCreatedAtDescCache() {
		// "evictTop5UserByCreatedAtDescCache" 캐시의 모든 데이터를 삭제
	}

	public void evictCacheUserPosts(Long id) {
		cacheManager.getCache("userPosts").evict(id);
		cacheManager.getCache("myPosts").evict(id);
	}

	public void evictCacheTagPosts(List<TagPost> tagPosts) {
		tagPosts.stream().forEach(tagPost -> {
			cacheManager.getCache("tagPosts").evict(tagPost.getTag().getId());
		});
	}

}
