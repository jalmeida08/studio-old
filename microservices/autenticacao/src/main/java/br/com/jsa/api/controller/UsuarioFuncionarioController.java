package br.com.jsa.api.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jsa.api.dto.UsuarioFuncionarioForm;
import br.com.jsa.api.service.UsuarioService;
import br.com.jsa.infra.model.Usuario;

@RestController
@RequestMapping("/funcionario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioFuncionarioController {
	
	@Autowired
	public UsuarioService usuarioService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> novoUsuarioFuncionario(@RequestBody UsuarioFuncionarioForm usuarioDTO) {
		this.usuarioService.salva(usuarioDTO);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public ResponseEntity<?> listaUsuario() {
		List<Usuario> u = usuarioService.listaUsuario();
		return ResponseEntity.ok(u);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscarUsuarioPorId(@PathVariable("id") String id){
		return ResponseEntity.ok(this.usuarioService.getUsuarioPorId(id));
	}
}
