package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
