package com.final_project_leesanghun_team2.repository.comment;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import com.final_project_leesanghun_team2.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

	Page<Comment> findAllByPostAndParentIsNull(Post post, Pageable pageable);
	Page<Comment> findAllByPostAndParentComment(Post post, Comment parentComment, Pageable pageable);

}
