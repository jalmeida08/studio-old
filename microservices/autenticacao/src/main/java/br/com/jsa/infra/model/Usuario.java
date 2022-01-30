package br.com.jsa.infra.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Document(collection = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 8622058730971581293L;

	@Id
	private String id;
	private String funcionarioId;
	private String clienteId;
	private String email;
	private String senha;
	private boolean ativo;
	@DBRef
	private List<Acesso> acesso = new ArrayList<Acesso>();
	@Version
	private Long versao;

	public String getFuncionarioId() {
		return funcionarioId;
	}

	public void setFuncionarioId(String funcionarioId) {
		this.funcionarioId = funcionarioId;
	}

	public String getClienteId() {
		return clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = new BCryptPasswordEncoder().encode(senha);
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Acesso> getAcesso() {
		return acesso;
	}

	public void setAcesso(List<Acesso> acesso) {
		this.acesso = acesso;
	}

	public String getId() {
		return id;
	}

	public Long getVersao() {
		return versao;
	}

}
