package org.sakaton.security.jwt.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author sakaton
 * @version created on 2020/9/5.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {


	private final PasswordEncoder passwordEncoder;

	public JwtUserDetailsService() {
		this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


		return User.builder().username("Jack").password(passwordEncoder.encode("jack-password")).roles("demo").build();
	}
}
