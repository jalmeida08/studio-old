package br.com.jsa.api.dto;

import java.time.LocalDate;

import br.com.jsa.infra.model.Cliente;

public class ClienteDTO {
	private String id;
	private String nome;
	private LocalDate dataNascimento;
	
	public ClienteDTO(Cliente c) {
		this.id = c.getId();
		this.nome = c.getNome();
		this.dataNascimento = c.getDataNascimento();
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
