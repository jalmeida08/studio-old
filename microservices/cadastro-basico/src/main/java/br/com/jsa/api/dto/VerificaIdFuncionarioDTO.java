package br.com.jsa.api.dto;

public class VerificaIdFuncionarioDTO {
	
	private String idFuncionario;
	private boolean valido;

	public VerificaIdFuncionarioDTO(String idFuncionario, boolean valido) {
		this.idFuncionario = idFuncionario;
		this.valido = valido;
	}

	public String getIdFuncionario() {
		return idFuncionario;
	}

	public boolean isValido() {
		return valido;
	}
	
	
}
