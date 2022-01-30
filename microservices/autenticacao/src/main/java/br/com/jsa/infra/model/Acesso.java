package br.com.jsa.infra.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "acesso")
public class Acesso {

	@Id
	private Long id;
	private String nome;
	@Version
	private Integer versao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public Integer getVersao() {
		return versao;
	}

}
