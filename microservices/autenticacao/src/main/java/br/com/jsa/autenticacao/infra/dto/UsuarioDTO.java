package br.com.jsa.autenticacao.infra.dto;

import br.com.jsa.autenticacao.infra.model.Usuario;

public class UsuarioDTO {
	
	private String email;
	private String senha;
	
	public Usuario toUsuario() {
		Usuario u = new Usuario();
		u.setEmail(this.email);
		u.setSenha(this.senha);
		return u;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	
}
