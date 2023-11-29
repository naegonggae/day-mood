package com.final_project_leesanghun_team2.security.service;

import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.user.DuplicateUsernameException;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.repository.UserRepository;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername 실행");

		User user = userRepository.findByUsername(username)
				.orElseThrow(NoSuchUserException::new);
		log.info("user = " + user.getUsername());
		log.info("loadUserByUsername 종료");

		return new PrincipalDetails(user);
	}
}
