package br.com.jsa.api.dto;

import java.time.LocalDate;

import br.com.jsa.infra.model.Usuario;

public class UsuarioForm {
	
	private String nome;
	private LocalDate dataNascimento;
	private String email;
	private String senha;
	
	public UsuarioForm() {}
	
	public FuncionarioForm toFuncionarioForm(){
		return new FuncionarioForm(this.nome, this.dataNascimento); 
	}

	public Usuario toUsuario(String funcionarioId) {
		Usuario u = new Usuario();
		u.setEmail(this.email);
		u.setSenha(this.senha);
		u.setFuncionarioId(funcionarioId);
		return u;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

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

}
