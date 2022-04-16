package br.com.jsa.infra.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfigure extends ResourceServerConfigurerAdapter {
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
    	.sessionCreationPolicy(SessionCreationPolicy.NEVER)
			.and().authorizeRequests()
			.antMatchers(HttpMethod.POST, "/funcionario").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated();
//		http.sessionManagement()
//        	.sessionCreationPolicy(SessionCreationPolicy.NEVER)
//        .and()
//		.authorizeRequests()
//			.antMatchers(HttpMethod.PUT, "/funcionario**").authenticated()
//			.antMatchers(HttpMethod.GET, "/funcionario/lista").authenticated()
//			.antMatchers("/acesso**").authenticated();
	}
}
