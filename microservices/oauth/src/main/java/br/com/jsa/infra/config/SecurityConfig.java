package br.com.jsa.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        	.antMatcher("/**")
	        	.authorizeRequests()
	        	.antMatchers("/oauth/authorize**", "/login**", "/error**")
	        	.permitAll()
        	.and()
            	.authorizeRequests()
            	.anyRequest().authenticated();
//        	.and()
//        		.formLogin().permitAll();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}