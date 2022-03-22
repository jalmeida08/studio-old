package br.com.jsa.api.form;

import java.time.LocalDate;

import br.com.jsa.infra.model.Cliente;

public class ClienteForm {

	private String nome;
	private LocalDate dataNascimento;

	public Cliente toCliente() {
		Cliente c = new Cliente();
		c.setNome(this.nome);
		c.setDataNascimento(this.dataNascimento);
		return c;
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
