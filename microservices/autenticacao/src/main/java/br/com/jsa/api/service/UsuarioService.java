package br.com.jsa.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.jsa.api.client.FuncionarioClient;
import br.com.jsa.api.dto.AlteraSenhaUsuarioForm;
import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.UsuarioForm;
import br.com.jsa.dominio.usuario.AlteraSenhaBO;
import br.com.jsa.infra.exception.EmailCadastradoRuntimeException;
import br.com.jsa.infra.model.Usuario;
import br.com.jsa.infra.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	public UsuarioRepository usuarioRepository;
	
	@Autowired
	public FuncionarioClient funcionarioClient;

	public void salva(UsuarioForm usuarioForm) {
		if(this.buscarPorEmail(usuarioForm.getEmail()).isPresent())
			throw new EmailCadastradoRuntimeException("E-mail já cadastrado");
		
		FuncionarioDTO dto = 
				this.funcionarioClient.cadastraDadosFuncionario(usuarioForm.toFuncionarioForm());
		Usuario u = usuarioForm.toUsuario(dto.getId());
		u.setAtivo(false);
		
		this.usuarioRepository.save(u);
		
	}

	public List<Usuario> listaUsuario() {
		return usuarioRepository.findAll();
	}
	
	public Usuario getUsuarioPorId(String id) {
		return usuarioRepository
				.findById(id)
				.orElseThrow(() -> new RuntimeException());
	}
	
	public Optional<Usuario> buscarPorEmail(String email) {
		return this.usuarioRepository.findByEmail(email);
	}

	public void alteraSenhaUsuario(AlteraSenhaUsuarioForm alteraSenhaUsuarioForm) {
		Usuario u = usuarioRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).get();
		new AlteraSenhaBO(alteraSenhaUsuarioForm, u.getSenha());
		
		u.setSenha(alteraSenhaUsuarioForm.getNovaSenha());
		this.usuarioRepository.save(u);
	}

}
