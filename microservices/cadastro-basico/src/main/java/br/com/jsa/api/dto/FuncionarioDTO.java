package br.com.jsa.api.dto;

import java.time.LocalDate;

import br.com.jsa.infra.model.Funcionario;

public class FuncionarioDTO {

	private String id;
	private String nome;
	private LocalDate dataNascimento;
	
	public FuncionarioDTO(Funcionario f) {
		this.id = f.getId();
		this.nome = f.getNome();
		this.dataNascimento = f.getDataNascimento();
	}
	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	
}
