package br.com.jsa.api.dto;

import br.com.jsa.infra.model.Acesso;

public class AcessoDTO {
	
	private String id;
	private String nome;
	private String descricao;
	
	public AcessoDTO(Acesso a) {
		id = a.getId();
		nome = a.getNome();
		descricao = a.getDescricao();
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}
	
	

}
