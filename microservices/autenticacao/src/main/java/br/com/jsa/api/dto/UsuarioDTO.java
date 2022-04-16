package br.com.jsa.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.jsa.infra.model.Usuario;

public class UsuarioDTO {
	
	private String id;
	private String funcionarioId;
	private String clienteId;
	private String email;
	private List<AcessoDTO> acesso;
	
	public UsuarioDTO(Usuario u) {
		this.id = u.getId();
		this.funcionarioId = u.getFuncionarioId();
		this.clienteId = u.getClienteId();
		this.email = u.getEmail();
		this.acesso = u.getAcesso().stream().map(AcessoDTO::new).collect(Collectors.toList());
	}
	
	public String getId() {
		return id;
	}
	public String getFuncionarioId() {
		return funcionarioId;
	}
	public String getClienteId() {
		return clienteId;
	}
	public String getEmail() {
		return email;
	}
	public List<AcessoDTO> getAcesso() {
		return acesso;
	}
	
	
	
}
