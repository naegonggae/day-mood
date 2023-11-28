package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class FollowRestController {

	private final FollowService followService;

	@PostMapping("/{userId}/follows")
	public ResponseEntity<Void> follow(@PathVariable Long userId,
			@AuthenticationPrincipal PrincipalDetails details) {
		followService.follow(userId, details.getUser());
		return ResponseEntity.noContent().build();
	}

}
