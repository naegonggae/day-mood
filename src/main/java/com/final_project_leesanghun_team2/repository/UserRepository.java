package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByNickName(String nickName);
    Optional<User> findByUsername(String userName);
    Optional<User> findByNickName(String NickName);
    List<User> findTop5ByOrderByCreatedAtDesc();

}
