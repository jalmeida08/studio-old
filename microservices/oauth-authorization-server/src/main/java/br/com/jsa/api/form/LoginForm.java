package br.com.jsa.api.form;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginForm {

	private String email;
	private String senha;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public UsernamePasswordAuthenticationToken toConverter() {
		return new UsernamePasswordAuthenticationToken(email.toLowerCase().trim(), senha);
	}

	@Override
	public String toString() {
		return "LoginDTO [\n" + "email=" + email + ",\n" + "senha=" + senha + "\n]";
	}

}
