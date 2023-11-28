package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Follow;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.repository.FollowRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	// 팔로우 누르기
	@Transactional
	public void follow(Long userId, User user) {

		User findUser = userRepository.findById(userId)
				.orElseThrow(NoSuchUserException::new);

		boolean isFollow = followRepository.existsByFollowUserAndFollowingUser(findUser, user);
		if (isFollow) {
			followRepository.deleteByFollowUserAndFollowingUser(findUser, user);
			user.decreaseFollowNum();
		}
		else {
			Follow follow = Follow.createFollow(findUser, user);
			followRepository.save(follow);
			user.increaseFollowNum();
		}
	}
}
