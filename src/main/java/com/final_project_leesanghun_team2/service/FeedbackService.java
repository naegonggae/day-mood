package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.feedback.FeedbackRequest;
import com.final_project_leesanghun_team2.domain.dto.feedback.FeedbackResponse;
import com.final_project_leesanghun_team2.domain.entity.Feedback;
import com.final_project_leesanghun_team2.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackService {

	private final FeedbackRepository feedBackRepository;

	@Transactional
	public FeedbackResponse saveFeedback(FeedbackRequest request) {

		Feedback feedback = Feedback.createdFeedback(request);
		Feedback savedFeedback = feedBackRepository.save(feedback);

		return FeedbackResponse.from(savedFeedback);
	}
}
