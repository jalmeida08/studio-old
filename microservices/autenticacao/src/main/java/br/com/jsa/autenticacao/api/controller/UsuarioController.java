package br.com.jsa.autenticacao.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jsa.autenticacao.api.dto.UsuarioDTO;
import br.com.jsa.autenticacao.api.service.UsuarioService;
import br.com.jsa.autenticacao.infra.model.Usuario;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
public class UsuarioController {
	
	@Autowired
	public UsuarioService usuarioService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> novoUsuario(@RequestBody UsuarioDTO usuarioDTO) {
		this.usuarioService.salva(usuarioDTO.toUsuario());
		return ResponseEntity.ok("Sucesso");
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
