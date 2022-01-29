package br.com.jsa.api.dto;

import java.time.LocalDate;

public class FuncionarioForm {

	private String nome;
	private LocalDate dataNascimento;

	public FuncionarioForm() {}
	
	public FuncionarioForm(String nome, LocalDate dataNascimento) {
		this.nome = nome;
		this.dataNascimento = dataNascimento;
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


	
}
