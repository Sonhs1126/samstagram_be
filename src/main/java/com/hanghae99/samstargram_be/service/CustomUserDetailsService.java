package com.hanghae99.samstargram_be.service;

import com.hanghae99.samstargram_be.model.Member;
import com.hanghae99.samstargram_be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

//*
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByUsername(username)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
	}

	private UserDetails createUserDetails(Member member) {
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

		return new User(
				String.valueOf(member.getMemberId()),
				member.getPassword(),
				Collections.singleton(grantedAuthority)
		);
	}
}
