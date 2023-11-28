package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.tag.TagSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.tag.TagSaveResponse;
import com.final_project_leesanghun_team2.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagRestController {

	private final TagService tagService;

	@PostMapping
	public ResponseEntity<Response<TagSaveResponse>> save(TagSaveRequest request) {
		TagSaveResponse result = tagService.save(request);
		return ResponseEntity.ok().body(Response.success(result));
	}

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<Void> delete(@PathVariable Long postId, String tagName) {
		tagService.delete(tagName, postId);
		return ResponseEntity.noContent().build();
	}

}
