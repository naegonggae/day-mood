package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.TagPost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheEvictService {

	private final CacheManager cacheManager;

	@CacheEvict(cacheNames = "top5JoinUserList", allEntries = true)
	public void evictTop5JoinUserList() {
		log.info("evictTop5JoinUserList 캐시를 삭제했습니다.");

	}

	@CacheEvict(cacheNames = "top5UserHasMostPostsList", allEntries = true)
	public void evictTop5UserHasMostPostsList() {
		log.info("top5UserHasMostPostsList 캐시를 삭제했습니다.");

	}

	@CacheEvict(cacheNames = "top5UserHasMostFollowingList", allEntries = true)
	public void evictTop5UserHasMostFollowingList() {
		log.info("top5UserHasMostFollowingList 캐시를 삭제했습니다.");

	}

	public void evictUserPosts(Long userId) {
		cacheManager.getCache("userPosts").evict(userId);
		cacheManager.getCache("myPosts").evict(userId);
		log.info("userPosts, myPosts - userId : {}의 캐시를 삭제했습니다.", userId);

	}

	public void evictTagPosts(List<TagPost> tagPosts) {
		tagPosts.stream().forEach(tagPost -> {
			cacheManager.getCache("tagPosts").evict(tagPost.getTag().getId());
			log.info("tagPosts - tagId : {}의 캐시를 삭제했습니다.", tagPost.getTag().getId());
		});
	}

}
