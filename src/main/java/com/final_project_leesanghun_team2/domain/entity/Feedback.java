package com.final_project_leesanghun_team2.domain.entity;

import com.final_project_leesanghun_team2.domain.dto.feedback.FeedbackRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends BaseEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 500)
	private String content;

	// 생성메서드
	public static Feedback createdFeedback(FeedbackRequest request) {
		return new Feedback(request);
	}
	public Feedback(FeedbackRequest request) {
		this.title = request.getTitle();
		this.content = request.getContent();
	}
}
