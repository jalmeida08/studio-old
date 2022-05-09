package br.com.jsa.api.dto;

import br.com.jsa.infra.model.Procedimento;

public class ProcedimentoAtendimentoDTO {

	private String id;
	private String nome;
	private Double valor;
	private boolean ativo;
	
	public ProcedimentoAtendimentoDTO(Procedimento p) {
		this.id = p.getId();
		this.nome = p.getNome();
		this.valor = p.getValor();
		this.ativo = p.isAtivo();
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Double getValor() {
		return valor;
	}


	public boolean isAtivo() {
		return ativo;
	}

}
