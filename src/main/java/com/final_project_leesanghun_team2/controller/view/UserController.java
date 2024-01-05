package com.final_project_leesanghun_team2.controller.view;

import com.final_project_leesanghun_team2.domain.dto.post.PostFindResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserFindResponse;
import com.final_project_leesanghun_team2.service.PostService;
import com.final_project_leesanghun_team2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;
	private final PostService postService;

	// 유저 정보 상세 페이지
	@GetMapping("/userInfo/{userId}/loginUser/{id}")
	public String userInfo(
			@PathVariable Long userId,
			@PathVariable Long id, Model model,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		UserFindResponse user = userService.findUser(userId, id);
		log.info("findUserPost 시작");
		Page<PostFindResponse> posts = postService.findUserPosts(pageable, userId, id);
		log.info("findUserPost 끝");

		model.addAttribute("user", user);
		model.addAttribute("posts", posts);
		return "users/userInfo";
	}

	// 나의 상세 페이지
	@GetMapping("/users/myInfo/{userId}")
	public String myInfo(
			@PathVariable Long userId,
			Model model,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		UserFindResponse user = userService.findUser(userId, userId);
		log.info("findUserPost 시작");
		Page<PostFindResponse> posts = postService.findUserPosts(pageable, userId, userId);
		log.info("findUserPost 끝");

		model.addAttribute("user", user);
		model.addAttribute("posts", posts);

		return "users/myInfo";
	}
}
