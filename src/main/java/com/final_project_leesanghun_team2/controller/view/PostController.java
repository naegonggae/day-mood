package com.final_project_leesanghun_team2.controller.view;

import com.final_project_leesanghun_team2.domain.dto.post.PostDefaultResponse;
import com.final_project_leesanghun_team2.domain.dto.post.PostFindResponse;
import com.final_project_leesanghun_team2.domain.dto.user.Top5JoinUserListResponse;
import com.final_project_leesanghun_team2.domain.dto.user.Top5UserHasMostFollowingListResponse;
import com.final_project_leesanghun_team2.domain.dto.user.Top5UsersHasMostPostsListResponse;
import com.final_project_leesanghun_team2.service.PostService;
import com.final_project_leesanghun_team2.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

	private final UserService userService;
	private final PostService postService;

	// 홈
	@GetMapping
	public String index(
			Model model,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		Page<PostDefaultResponse> posts = postService.findAllPosts(pageable);
		List<Top5JoinUserListResponse> newUsers = userService.top5JoinUserList();
		List<Top5UsersHasMostPostsListResponse> mostPostsUsers = userService.top5UserHasMostPostsList();
		List<Top5UserHasMostFollowingListResponse> followerUsers = userService.top5UserHasMostFollowingList();

		model.addAttribute("posts", posts);
		model.addAttribute("newUsers", newUsers);
		model.addAttribute("mostPostsUsers", mostPostsUsers);
		model.addAttribute("followerUsers", followerUsers);
		model.addAttribute("currentPage", pageable.getPageNumber()); // 현재 페이지 번호
		model.addAttribute("totalPages", posts.getTotalPages()); // 전체 페이지 수

		return "index";
	}

	// 로그인 후 홈
	@GetMapping("/{id}")
	public String indexWhenLogin(
			@PathVariable Long id, Model model,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

		Page<PostFindResponse> posts = postService.findAllPostsWhenLogin(pageable, id);
		List<Top5JoinUserListResponse> newUsers = userService.top5JoinUserList();
		List<Top5UsersHasMostPostsListResponse> mostPostsUsers = userService.top5UserHasMostPostsList();
		List<Top5UserHasMostFollowingListResponse> followerUsers = userService.top5UserHasMostFollowingList();

		model.addAttribute("posts", posts);
		model.addAttribute("userId", id);
		model.addAttribute("newUsers", newUsers);
		model.addAttribute("mostPostsUsers", mostPostsUsers);
		model.addAttribute("followerUsers", followerUsers);
		model.addAttribute("currentPage", pageable.getPageNumber()); // 현재 페이지 번호
		model.addAttribute("totalPages", posts.getTotalPages()); // 전체 페이지 수

		return "indexWhenLogin";
	}

	// 게시물 생성 페이지
	@GetMapping("/posts/new")
	public String createPostForm() {
		return "posts/postForm";
	}

	// 태그로 게시물 검색결과 페이지
	@GetMapping("/search/{id}")
	public String searchPost(
			@PathVariable Long id,
			@RequestParam(name = "keyword", required = false) String keyword, Model model,
			@PageableDefault(size = 10)
			@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		log.info("findByTag 시작");
		Page<PostFindResponse> posts = postService.findPostsByTag(pageable, keyword, id);
		log.info("findByTag 끝");

		model.addAttribute("keyword", keyword); // 키워드 파람
		model.addAttribute("posts", posts);
		model.addAttribute("userId", id);
		model.addAttribute("currentPage", pageable.getPageNumber()); // 현재 페이지 번호
		model.addAttribute("totalPages", posts.getTotalPages()); // 전체 페이지 수

		return "posts/postSearch";
	}
}
