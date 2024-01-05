package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.tag.TagFindResponse;
import com.final_project_leesanghun_team2.exception.tag.NoSuchTagException;
import com.final_project_leesanghun_team2.domain.dto.tag.TagSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.tag.TagSaveResponse;
import com.final_project_leesanghun_team2.domain.entity.Tag;
import com.final_project_leesanghun_team2.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TagService {

	private final TagRepository tagRepository;

	// 태그 생성
	@Transactional
	public TagSaveResponse saveTag(TagSaveRequest request) {

		log.info("tag 저장 시작");
		log.info(request.getName());

		// 1. 태그를 조회
		// 2. 있음 -> 태그 사용
		//    없음 -> 태그 생성
		Tag findTag = tagRepository.findByName(request.getName())
				.orElseGet(() -> {
					log.info("조회했더니 없어서 생성하러 들어옴");
					Tag tag = Tag.createTag(request);
					return tagRepository.save(tag);
				});

		log.info("tag 저장 종료");

		return TagSaveResponse.from(findTag);
	}

	// 태그 존재 여부 확인
	public TagFindResponse isTagExists(String tagName) {

		// 태그 존재 여부
		boolean result = tagRepository.existsByName(tagName);

		// 존재하지 않는 태그로 검색 할 수 없다.
		if (!result) throw new NoSuchTagException();

		// 태그가 존재하면 True 를 리턴한다.
		return TagFindResponse.from(result);
	}

	// 태그 삭제
	@Transactional
	public void deleteTag(Long tagId) {

		// 태그
		Tag findTag = tagRepository.findById(tagId).orElseThrow(NoSuchTagException::new);

		// 태그 삭제
		tagRepository.delete(findTag);
	}

}