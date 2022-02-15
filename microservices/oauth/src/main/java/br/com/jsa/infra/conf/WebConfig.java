package br.com.jsa.infra.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
public class WebConfig {
	
	@Autowired
	private CustomAuthenticationProvider provider;
	
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(provider);
    }

}
