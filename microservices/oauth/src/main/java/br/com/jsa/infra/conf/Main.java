package br.com.jsa.infra.conf;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123123"));
		System.out.println(new BCryptPasswordEncoder().matches("123123", "$2a$10$akuurfMfPDMo9TgFov.ose3lajVPptXPC.NS1l/NzXQQMpACLBwIm"));

	}

}
