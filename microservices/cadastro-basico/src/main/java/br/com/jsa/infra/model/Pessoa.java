package br.com.jsa.infra.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Pessoa {

	private String nome;
	private LocalDate dataNascimento;
	private List<Contato> contato = new ArrayList<>();
	
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
	public List<Contato> getContato() {
		return contato;
	}
	public void setContato(List<Contato> contato) {
		this.contato = contato;
	}
	
	
}
