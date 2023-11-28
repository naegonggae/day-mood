package com.final_project_leesanghun_team2.security.service;

import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.user.DuplicateUsernameException;
import com.final_project_leesanghun_team2.repository.UserRepository;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService - loadUserByUsername 실행");

		User user = userRepository.findByUsername(username)
				.orElseThrow(DuplicateUsernameException::new);
		System.out.println("PrincipalDetailsService - user = " + user.getUsername());
		System.out.println("PrincipalDetailsService - loadUserByUsername 종료");

		return new PrincipalDetails(user);
	}
}
