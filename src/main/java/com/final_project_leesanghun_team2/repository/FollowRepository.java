package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Follow;
import com.final_project_leesanghun_team2.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	boolean existsByFollowUserAndFollowingUser(User followUser, User followingUser);
	void deleteByFollowUserAndFollowingUser(User followUser, User followingUser);

}