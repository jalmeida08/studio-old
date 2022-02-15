package br.com.jsa.api.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.jsa.infra.model.Acesso;
import br.com.jsa.infra.model.Usuario;

public class UsuarioDTO {

	private String id;
	private String email;
	private List<Acesso> acesso = new ArrayList<Acesso>();

	public UsuarioDTO(Usuario u) {
		this.id = u.getId();
		this.email = u.getEmail();
		this.acesso = u.getAcesso();
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public List<Acesso> getAcesso() {
		return acesso;
	}

}
