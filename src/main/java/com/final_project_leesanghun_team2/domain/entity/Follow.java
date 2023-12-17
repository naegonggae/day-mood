package com.final_project_leesanghun_team2.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Follow extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follow_user_id")
	private User followUser; // 너

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_user_id")
	private User followingUser; // 나

	// 생성 메서드
	public static Follow createFollow(User findUser, User user) {
		return new Follow(findUser, user);
	}
	public Follow(User followUser, User followingUser) {
		this.followUser = followUser;
		this.followingUser = followingUser;
	}
}
