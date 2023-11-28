package com.final_project_leesanghun_team2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.final_project_leesanghun_team2.domain.dto.tag.TagSaveRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long id;

	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TagPost> tagPostList = new ArrayList<>();

	// 생성 메서드
	public static Tag createTag(TagSaveRequest request) {
		return new Tag(request);
	}
	public Tag(TagSaveRequest request) {
		this.name = request.getName();
	}
}
