package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.tag.TagFindResponse;
import com.final_project_leesanghun_team2.domain.dto.tag.TagSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.tag.TagSaveResponse;
import com.final_project_leesanghun_team2.service.TagService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagRestController {

	private final TagService tagService;

	// 태그 생성
	@PostMapping
	public ResponseEntity<Response<TagSaveResponse>> saveTag(@RequestBody TagSaveRequest request) {
		TagSaveResponse result = tagService.saveTag(request);
		return ResponseEntity.created(URI.create("api/v1/tags/"+result.getId()))
				.body(Response.success(result));
	}

	// 존재하는 태그인지 체크
	@GetMapping
	public ResponseEntity<Response<TagFindResponse>> isExist(@RequestParam(name = "tagName") String tagName) {
		TagFindResponse result = tagService.isExistTag(tagName);
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 태그 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
		tagService.deleteTag(id);
		return ResponseEntity.noContent().build();
	}

}