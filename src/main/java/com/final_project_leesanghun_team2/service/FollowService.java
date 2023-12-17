package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.follow.FollowPeopleResponse;
import com.final_project_leesanghun_team2.domain.dto.follow.FollowResponse;
import com.final_project_leesanghun_team2.domain.dto.follow.IsFollowResponse;
import com.final_project_leesanghun_team2.domain.entity.Follow;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.follow.SelfFollowException;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.repository.FollowRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	// 팔로우 누르기
	@Transactional
	public FollowResponse follow(Long userId, User user) {

		// 너
		User findUser = userRepository.findById(userId)
				.orElseThrow(NoSuchUserException::new);

		// UserDetail 값을 영속성에 가져옴
		// 나
		User me = userRepository.findByUsername(user.getUsername())
				.orElseThrow(NoSuchUserException::new);

		// 내가 나를 팔로우 할 수 없음
		if (Objects.equals(findUser.getUsername(), me.getUsername())) throw new SelfFollowException();

		boolean isFollow = followRepository.existsByFollowUserAndFollowingUser(findUser, me);

		// 팔로우 버튼 누른 상태 -> 팔로우 객체를 삭제
		if (isFollow) {
			followRepository.deleteByFollowUserAndFollowingUser(findUser, me);
			// 나의 팔로워 팔로잉인지 너의 팔로워 팔로잉이지 기준잡아야 안헷갈림
			me.decreaseFollowingNum();
			findUser.decreaseFollowNum();
			return FollowResponse.from(false, findUser.getFollowNum(), me.getFollowingNum());

		}
		// 팔로우 버튼 안누른 상태 -> 팔로우 객체를 생성
		else {
			Follow follow = Follow.createFollow(findUser, me);
			followRepository.save(follow);
			me.increaseFollowingNum();
			findUser.increaseFollowNum();
			return FollowResponse.from(true, findUser.getFollowNum(), me.getFollowingNum());
		}
	}

	// 로그인한 사람이 user id 사람을 팔로우하는지 체크
	public IsFollowResponse isFollow(Long userId, Long id) {

		User findUser = userRepository.findById(userId)
				.orElseThrow(NoSuchUserException::new);

		User me = userRepository.findById(id)
				.orElseThrow(NoSuchUserException::new);

		boolean isFollow = followRepository.existsByFollowUserAndFollowingUser(findUser, me);
		return IsFollowResponse.from(isFollow);
	}

	// 팔로잉 목록 조회
	public Page<FollowPeopleResponse> findFollowingPeople(Long userId, Pageable pageable, User user) {
		User findUser = userRepository.findById(userId)
				.orElseThrow(NoSuchUserException::new);

		User me = userRepository.findById(user.getId())
				.orElseThrow(NoSuchUserException::new);

		Page<FollowPeopleResponse> followingResponsePage = followRepository
				.findAllByFollowUser(findUser, pageable)
				.map(follow -> FollowPeopleResponse.following(
						follow,
						followRepository.existsByFollowUserAndFollowingUser(follow.getFollowingUser(), me),
						Objects.equals(follow.getFollowingUser().getUsername(), me.getUsername())));
		return followingResponsePage;
	}

	// 팔로우 목록 조회
	public Page<FollowPeopleResponse> findFollowPeople(Long userId, Pageable pageable, User user) {
		User findUser = userRepository.findById(userId)
				.orElseThrow(NoSuchUserException::new);

		User me = userRepository.findById(user.getId())
				.orElseThrow(NoSuchUserException::new);

		Page<FollowPeopleResponse> followResponsePage = followRepository
				.findAllByFollowingUser(findUser, pageable)
				.map(follow -> FollowPeopleResponse.follow(
						follow,
						followRepository.existsByFollowUserAndFollowingUser(follow.getFollowUser(), me),
						Objects.equals(follow.getFollowUser().getUsername(), me.getUsername())));
		return followResponsePage;
	}
}
