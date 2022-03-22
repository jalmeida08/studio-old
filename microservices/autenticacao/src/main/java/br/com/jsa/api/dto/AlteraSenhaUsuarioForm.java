package br.com.jsa.api.dto;

public class AlteraSenhaUsuarioForm {
	
	public String senhaAntiga;
	public String novaSenha;
	
	
	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}
	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
	public String getSenhaAntiga() {
		return senhaAntiga;
	}
	public String getNovaSenha() {
		return novaSenha;
	}
	
	

}
