package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.follow.FollowPeopleResponse;
import com.final_project_leesanghun_team2.domain.dto.follow.FollowResponse;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class FollowRestController {

	private final FollowService followService;

	// 팔로우 이벤트
	@PostMapping("/{userId}/follows")
	public ResponseEntity<Response<FollowResponse>> follow(@PathVariable Long userId,
			@AuthenticationPrincipal PrincipalDetails details) {
		FollowResponse result = followService.follow(userId, details.getUser());
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 유저의 팔로우 목록
	@GetMapping("/{userId}/follows")
	public ResponseEntity<Response<Page<FollowPeopleResponse>>> findFollowPeople(@PathVariable Long userId,
			@AuthenticationPrincipal PrincipalDetails details,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt",direction = Direction.DESC) Pageable pageable) {
		Page<FollowPeopleResponse> result = followService.findFollowPeople(userId, pageable, details.getUser());
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 유저의 팔로잉 목록
	@GetMapping("/{userId}/followings")
	public ResponseEntity<Response<Page<FollowPeopleResponse>>> findFollowingPeople(@PathVariable Long userId,
			@AuthenticationPrincipal PrincipalDetails details,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt",direction = Direction.DESC) Pageable pageable) {
		Page<FollowPeopleResponse> result = followService.findFollowingPeople(userId, pageable, details.getUser());
		return ResponseEntity.ok().body(Response.success(result));
	}

}