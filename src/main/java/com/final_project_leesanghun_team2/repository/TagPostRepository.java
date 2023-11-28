package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.Tag;
import com.final_project_leesanghun_team2.domain.entity.TagPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagPostRepository extends JpaRepository<TagPost, Long> {

	void deleteByTagAndPost(Tag tag, Post post);

}
