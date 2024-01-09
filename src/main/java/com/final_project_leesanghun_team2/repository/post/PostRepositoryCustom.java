package com.final_project_leesanghun_team2.repository.post;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

	Page<Post> findAllPost(Pageable pageable);
	Page<Post> findAllDefaultPost(Pageable pageable);
	List<User> findTop5UsersWithMostPosts();



}
