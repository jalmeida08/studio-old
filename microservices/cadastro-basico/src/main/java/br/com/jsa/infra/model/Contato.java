package br.com.jsa.infra.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contato")
public class Contato {

	@Id
	private String id;
	private int ddd;
	private Long numero;
	private Long versao;

	public int getDdd() {
		return ddd;
	}

	public void setDdd(int ddd) {
		this.ddd = ddd;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getId() {
		return id;
	}

	public Long getVersao() {
		return versao;
	}

}
