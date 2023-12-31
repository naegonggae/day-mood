package com.final_project_leesanghun_team2.configuration.cache;

import lombok.Getter;

@Getter
public enum CacheType {

	TOP5_JOIN_USER_LIST("top5JoinUserList"),
	TOP5_USER_HAS_MOST_POSTS_LIST("top5UserHasMostPostsList"),
	TOP5_USER_HAS_MOST_FOLLOWING_LIST("top5UserHasMostFollowingList"),
	MY_POSTS("myPosts"),
	USER_POSTS("userPosts"),
	TAG_POSTS("tagPosts")
	;

	private String cacheName;
	private int expireAfterWrite;
	private int maximumSize;

	CacheType(String cacheName) {
		this.cacheName = cacheName;
		this.expireAfterWrite = ConstConfig.DEFAULT_TTL_SEC;
		this.maximumSize = ConstConfig.DEFAULT_MAX_SIZE;
	}

	static class ConstConfig {
		static final int DEFAULT_TTL_SEC = 6000; // 데이터의 만료 시간, 10분
		static final int DEFAULT_MAX_SIZE = 10000; // 캐시의 최대 크기
	}

}