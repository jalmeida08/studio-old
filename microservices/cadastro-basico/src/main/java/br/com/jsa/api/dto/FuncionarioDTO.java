package br.com.jsa.api.dto;

import br.com.jsa.infra.model.Funcionario;
import lombok.Getter;

@Getter
public class FuncionarioDTO {

	private String id;
	private String nome;
	private String dataNascimento;
	
	public FuncionarioDTO(Funcionario f) {
		this.id = f.getId();
		this.nome = f.getNome();
		this.dataNascimento = f.getDataNascimento();
	}
	
}
