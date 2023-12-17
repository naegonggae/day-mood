package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.feedback.FeedbackRequest;
import com.final_project_leesanghun_team2.domain.dto.feedback.FeedbackResponse;
import com.final_project_leesanghun_team2.service.FeedbackService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedBackRestController {

	private final FeedbackService feedbackService;

	@PostMapping
	public ResponseEntity<Response<FeedbackResponse>> saveFeedback(@Valid @RequestBody FeedbackRequest request) {
		FeedbackResponse result = feedbackService.saveFeedback(request);
		return ResponseEntity.created(URI.create("api/v1/feedback/"+result.getId()))
				.body(Response.success(result));
	}

}
