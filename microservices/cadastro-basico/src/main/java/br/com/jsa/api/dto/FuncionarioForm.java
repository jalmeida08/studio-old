package br.com.jsa.api.dto;

import br.com.jsa.infra.model.Funcionario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionarioForm {

	private String nome;
	private String dataNascimento;

	public FuncionarioForm() {}
	
	public Funcionario toFuncionario() {
		Funcionario f = new Funcionario();
		f.setNome(this.nome);
		f.setDataNascimento(this.dataNascimento);
		return f;
	}
	
}
