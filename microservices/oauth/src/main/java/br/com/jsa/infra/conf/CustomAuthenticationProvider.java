package br.com.jsa.infra.conf;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.jsa.api.service.LoginService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private LoginService loginService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		UserDetails loadUserByUsername = loginService.loadUserByUsername(name);
		
		if(new BCryptPasswordEncoder().matches(password, loadUserByUsername.getPassword())) {
			return new UsernamePasswordAuthenticationToken(loadUserByUsername.getUsername(),
					loadUserByUsername.getPassword(), new ArrayList<>());			
		}else {
			throw new UsernameNotFoundException("Dados inválido");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}