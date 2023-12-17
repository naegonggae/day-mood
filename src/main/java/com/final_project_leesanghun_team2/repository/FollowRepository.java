package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Follow;
import com.final_project_leesanghun_team2.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	boolean existsByFollowUserAndFollowingUser(User followUser, User followingUser);
	void deleteByFollowUserAndFollowingUser(User followUser, User followingUser);
	Page<Follow> findAllByFollowingUser(User followingUser, Pageable pageable); // followingUser 를 팔로우하는 사람들, followUser 조회
	Page<Follow> findAllByFollowUser(User followUser, Pageable pageable); // followUser 를 팔로잉하는 사람들, followingUser 조회

}