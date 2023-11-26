package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.exception.ErrorCode;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import com.final_project_leesanghun_team2.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
        // http://localhost:8080/board/list?page=0
        // http://localhost:8080/board/list?page=0&size=1
        // Page<Board> boards = boardRepository.findAll(pageable);
        // http://localhost:8080/board/list?searchText=222
        Page<Post> posts = postRepository.findByTitleContainingOrBodyContaining(searchText, searchText, pageable);
        int startPage = Math.max(1, posts.getPageable().getPageNumber() - 4);
        int endPage = Math.min(posts.getTotalPages(), posts.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        //posts.getTotalElements();
        model.addAttribute("posts", posts);
        return "/posts/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Integer id) {
        if (id == null) {
            model.addAttribute("post", new Post());
        } else {
            Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException());
            model.addAttribute("post", post);
        }
        return "/posts/form";
    }

    @PostMapping("/form")
    public String submit(@Valid Post post, BindingResult bindingResult) {
        //PostValidator.validate(post, bindingResult); // 내용검증
        if (bindingResult.hasErrors()) { // NotNull, Size 검증
            return "posts/form";
        }
        postRepository.save(post);
        return "redirect:/posts/list";
    }

    @GetMapping("/modify")
    public String formModify(Model model, @RequestParam(required = false) Integer id) {
        if (id == null) {
            model.addAttribute("post", new Post());
        } else {
            Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException());
            model.addAttribute("post", post);
        }
        return "/posts/modify";
    }

    @PostMapping("/modify")
    public String submitModify(@Valid Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { // NotNull, Size 검증
            return "posts/modify";
        }
        postRepository.save(post);
        return "redirect:/posts/list";
    }

    @GetMapping("/delete")
    public String delete(Model model, @RequestParam(required = false) Integer id) {
        if (id == null) {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        } else {
            postRepository.deleteById(id);
        }
        return "redirect:/posts/list";
    }
}
