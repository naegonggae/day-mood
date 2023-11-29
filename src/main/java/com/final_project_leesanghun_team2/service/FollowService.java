package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Follow;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.follow.SelfFollowException;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.repository.FollowRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.util.Objects;
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

		// 영속성에 들어와야함
		User me = userRepository.findByUsername(user.getUsername())
				.orElseThrow(NoSuchUserException::new);

		// 내가 나를 팔로우 할 수 없음
		if (Objects.equals(findUser.getUsername(), me.getUsername())) throw new SelfFollowException();

		boolean isFollow = followRepository.existsByFollowUserAndFollowingUser(findUser, me);
		if (isFollow) {
			followRepository.deleteByFollowUserAndFollowingUser(findUser, me);
			me.decreaseFollowNum(); // 팔로우 감소
			findUser.decreaseFollowingNum(); // 팔로워 감소

		}
		else {
			Follow follow = Follow.createFollow(findUser, me);
			followRepository.save(follow);
			me.increaseFollowNum(); // 팔로우 증가
			findUser.increaseFollowingNum(); // 팔로워 증가
		}
	}
}
