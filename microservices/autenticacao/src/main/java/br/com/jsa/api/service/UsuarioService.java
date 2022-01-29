package br.com.jsa.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.api.client.FuncionarioClient;
import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.UsuarioFuncionarioForm;
import br.com.jsa.infra.model.Usuario;
import br.com.jsa.infra.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	public UsuarioRepository usuarioRepository;
	
	@Autowired
	public FuncionarioClient funcionarioClient;

	public void salva(UsuarioFuncionarioForm usuarioForm) {
		FuncionarioDTO dto = 
				this.funcionarioClient.cadastraDadosFuncionario(usuarioForm.toFuncionarioForm());
		this.usuarioRepository.save(usuarioForm.toUsuario(dto.getId()));
		
	}

	public List<Usuario> listaUsuario() {
		return usuarioRepository.findAll();
	}
	
	public Usuario getUsuarioPorId(String id) {
		return usuarioRepository
				.findById(id)
				.orElseThrow(() -> new RuntimeException());
	}

}
