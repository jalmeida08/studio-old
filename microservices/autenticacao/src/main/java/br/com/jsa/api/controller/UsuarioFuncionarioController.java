package br.com.jsa.api.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jsa.api.dto.AlteraSenhaUsuarioForm;
import br.com.jsa.api.dto.UsuarioDTO;
import br.com.jsa.api.dto.UsuarioForm;
import br.com.jsa.api.form.IdAcessoForm;
import br.com.jsa.api.service.UsuarioService;

@RestController
@RequestMapping("/funcionario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioFuncionarioController {
	
	@Autowired
	public UsuarioService usuarioService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> novoUsuarioFuncionario(@RequestBody UsuarioForm usuarioDTO) {
		this.usuarioService.salva(usuarioDTO);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CEO')")  
	public ResponseEntity<?> listaUsuario() {
		List<UsuarioDTO> u = usuarioService.listaUsuario();
		return ResponseEntity.ok(u);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscaUsuarioPorId(@PathVariable("id") String id){
		return ResponseEntity.ok(this.usuarioService.getUsuarioPorId(id));
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<?> alteraSenhaFuncionario( @RequestBody AlteraSenhaUsuarioForm alteraSenhaUsuarioForm) {
		this.usuarioService.alteraSenhaUsuario(alteraSenhaUsuarioForm);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{idUsuario}/concede-acesso")
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CEO')") 
	public ResponseEntity<?> concedeAcessoAUsuario(@PathVariable String idUsuario, @RequestBody IdAcessoForm acessoForm){
		usuarioService.concedeACesso(idUsuario, acessoForm.getAcesso());
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{idUsuario}/remove-acesso")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CEO')") 
	@Transactional
	public ResponseEntity<?> removeAcessoAUsuario(@PathVariable String idUsuario, @RequestBody IdAcessoForm acessoForm){
		usuarioService.removeACesso(idUsuario, acessoForm.getAcesso());
		return ResponseEntity.ok().build();
	}

}
