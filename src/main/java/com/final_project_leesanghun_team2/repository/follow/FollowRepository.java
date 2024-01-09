package com.final_project_leesanghun_team2.repository.follow;

import com.final_project_leesanghun_team2.domain.entity.Follow;
import com.final_project_leesanghun_team2.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

	boolean existsByFollowUserAndFollowingUser(User followUser, User followingUser);
	void deleteByFollowUserAndFollowingUser(User followUser, User followingUser);
	Long countByFollowingUser(User user); // 유저의 팔로잉 개수
	Long countByFollowUser(User user); // 유저의 팔로우 개수
	Page<Follow> findAllByFollowingUser(User user, Pageable pageable); // 유저의 팔로잉 목록
	Page<Follow> findAllByFollowUser(User user, Pageable pageable); // 유저의 팔로우 목록
}