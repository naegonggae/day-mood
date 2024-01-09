package com.final_project_leesanghun_team2.domain.dto.post;

import static java.util.stream.Collectors.toList;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.TagPost;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDefaultResponse {

	private Long id;
	private String content;
	private List<TagPostDto> tagList;
	private Long userId;
	private String userNickName;
	private int likeCount;

	public PostDefaultResponse(Post post) {
		this.id = post.getId();
		this.content = post.getContent();
		this.tagList = post.getTagPostList().stream()
				.map(t -> new TagPostDto(t))
				.collect(toList());
		this.userId = post.getUser().getId();
		this.userNickName = post.getUser().getNickName();
		this.likeCount = post.getLikeList().size();
	}

	@Getter
	static class TagPostDto {
		private String name;
		public TagPostDto(TagPost tagPost) {
			this.name = tagPost.getTag().getName();
		}
	}

}