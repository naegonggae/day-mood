package com.final_project_leesanghun_team2.controller.view;

import com.final_project_leesanghun_team2.domain.dto.follow.IsFollowResponse;
import com.final_project_leesanghun_team2.domain.dto.likes.LikesCheckResponse;
import com.final_project_leesanghun_team2.domain.dto.post.PostFindResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserFindResponse;
import com.final_project_leesanghun_team2.service.FollowService;
import com.final_project_leesanghun_team2.service.LikesService;
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
	private final FollowService followService;
	private final LikesService likesService;

	// 유저 정보 상세 페이지
	@GetMapping("/userInfo/{userId}/loginUser/{id}")
	public String userInfo(
			@PathVariable Long userId,
			@PathVariable Long id, Model model,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		UserFindResponse user = userService.findOne(userId);

		// 내가 팔로우 했는지 체크
		IsFollowResponse isFollow = followService.isFollow(userId, id);
		user.addIsPush(isFollow.isPush());

		Page<PostFindResponse> posts = getPostFindResponsePage(userId, id, pageable);

		model.addAttribute("user", user);
		model.addAttribute("posts", posts);
		return "users/userInfo";
	}

	// 나의 상세 페이지
	@GetMapping("/users/myInfo/{id}")
	public String myInfo(
			@PathVariable Long id,
			Model model,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		UserFindResponse user = userService.findOne(id);

		Page<PostFindResponse> posts = getPostFindResponsePage(id, id, pageable);

		model.addAttribute("user", user);
		model.addAttribute("posts", posts);

		return "users/myInfo";
	}

	private Page<PostFindResponse> getPostFindResponsePage(Long userId, Long id, Pageable pageable) {
		Page<PostFindResponse> posts = postService.findUserPost(pageable, userId);

		// post 마다 isPush 를 채워준다.
		posts.stream()
				.forEach(post -> {
					LikesCheckResponse likesCheckResponse = likesService.hasUserLikedPost(post.getId(), id);
					post.addIsPush(likesCheckResponse.isPush());
				});
		return posts;
	}
}
