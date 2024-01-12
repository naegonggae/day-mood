package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.follow.FollowPeopleResponse;
import com.final_project_leesanghun_team2.domain.dto.follow.FollowResponse;
import com.final_project_leesanghun_team2.domain.entity.Follow;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.follow.SelfFollowException;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.repository.follow.FollowRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	// 팔로우 이벤트
	@Transactional
	public FollowResponse follow(Long userId, User user) {

		// 유저
		User findUser = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

		// 로그인한 유저 영속성에 가져옴
		User loginUser = userRepository.findById(user.getId()).orElseThrow(NoSuchUserException::new);

		// 자기자신을 팔로우 할 수 없다
		if (Objects.equals(findUser.getUsername(), loginUser.getUsername())) throw new SelfFollowException();

		// 로그인한 유저가 유저를 팔로우 하는지 여부
		boolean isFollow = followRepository.existsByFollowUserAndFollowingUser(findUser, loginUser);

		// 로그인한 유저의 팔로잉 개수
		Long followingCount = followRepository.countByFollowingUser(loginUser);

		// 로그인한 유저의 팔로우 개수
		Long followCount = followRepository.countByFollowUser(findUser);

		// 팔로우 있음 -> 팔로우 삭제
		if (isFollow) {
			followRepository.deleteByFollowUserAndFollowingUser(findUser, loginUser);
			return FollowResponse.of(false, followCount-1, followingCount-1);
		}

		// 팔로우 없음 -> 팔로우 생성
		else {
			Follow follow = Follow.createFollow(findUser, loginUser);
			followRepository.save(follow);
			return FollowResponse.of(true, followCount+1, followingCount+1);
		}
	}

	// 유저의 팔로우 목록
	public Page<FollowPeopleResponse> findFollowPeople(Long userId, Pageable pageable, User user) {

		// 유저
		User findUser = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

		// 로그인한 유저
		User loginUser = userRepository.findById(user.getId()).orElseThrow(NoSuchUserException::new);

		// 유저의 팔로우 목록
		return followRepository.findAllByFollowUser(findUser, pageable)
				.map(follow -> FollowPeopleResponse.following(
						follow,
						followRepository.existsByFollowUserAndFollowingUser(follow.getFollowingUser(), loginUser), // 팔로잉하는 유저가 로그인한 유저를 팔로우 했는지 여부
						Objects.equals(follow.getFollowingUser().getUsername(), loginUser.getUsername()))); // 팔로잉하는 유저가 로그인한 유저인지 여부
	}

	// 유저의 팔로잉 목록
	public Page<FollowPeopleResponse> findFollowingPeople(Long userId, Pageable pageable, User user) {

		// 유저
		User findUser = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

		// 로그인한 유저
		User loginUser = userRepository.findById(user.getId()).orElseThrow(NoSuchUserException::new);

		// 유저의 팔로잉 목록
		return followRepository.findAllByFollowingUser(findUser, pageable)
				.map(follow -> FollowPeopleResponse.follow(
						follow,
						followRepository.existsByFollowUserAndFollowingUser(follow.getFollowUser(), loginUser),
						Objects.equals(follow.getFollowUser().getUsername(), loginUser.getUsername())));
	}
}
