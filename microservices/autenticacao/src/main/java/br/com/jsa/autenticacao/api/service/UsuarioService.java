package br.com.jsa.autenticacao.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.autenticacao.infra.model.Usuario;
import br.com.jsa.autenticacao.infra.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	public UsuarioRepository usuarioRepository;

	public void salva(Usuario usuario) {
		this.usuarioRepository.save(usuario);
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
