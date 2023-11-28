package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

	boolean existsByName(String tagName);

	Optional<Tag> findByName(String tagName);

}
