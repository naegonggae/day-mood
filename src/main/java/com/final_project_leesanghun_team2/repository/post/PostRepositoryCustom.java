package com.final_project_leesanghun_team2.repository.post;

import com.final_project_leesanghun_team2.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

	Page<Post> findAllPost(Pageable pageable);

}
