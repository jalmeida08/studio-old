package br.com.jsa.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.infra.model.Usuario;
import br.com.jsa.infra.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario buscarPorId(String idUsuario) {
		return usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new RuntimeException("Parametro não econtrado"));
	}

}
