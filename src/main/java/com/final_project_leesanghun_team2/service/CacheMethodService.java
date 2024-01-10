package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.Tag;
import com.final_project_leesanghun_team2.domain.entity.TagPost;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.repository.post.PostRepository;
import com.final_project_leesanghun_team2.repository.TagPostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CacheMethodService {

	private final PostRepository postRepository;
	private final TagPostRepository tagPostRepository;

	@Cacheable(cacheNames = "tagPosts", key = "#findTag.id")
    public List<TagPost> getTagPosts(Tag findTag) {
        return tagPostRepository.findAllByTag(findTag);
    }

	@Cacheable(cacheNames = "myPosts", key = "#user.id")
	public Page<Post> getMyPosts(Pageable pageable, User user) {
		return postRepository.findAllByUser(user, pageable);
	}

//	@Cacheable(cacheNames = "userPosts", key = "#user.id")
	public Page<Post> getUserPosts(Pageable pageable, User user) {
		return postRepository.findAllByUser(user, pageable);
	}
}
