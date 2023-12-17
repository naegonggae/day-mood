package com.final_project_leesanghun_team2.domain.dto.feedback;

import com.final_project_leesanghun_team2.domain.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

	private Long id;

	public static FeedbackResponse from(Feedback feedback) {
		return new FeedbackResponse(feedback.getId());
	}

}
