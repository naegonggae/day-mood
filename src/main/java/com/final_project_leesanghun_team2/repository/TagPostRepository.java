package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Tag;
import com.final_project_leesanghun_team2.domain.entity.TagPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagPostRepository extends JpaRepository<TagPost, Long> {

	Page<TagPost> findAllByTag(Tag tag, Pageable pageable);
}
