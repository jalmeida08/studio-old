package br.com.jsa.api.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.FuncionarioForm;
import br.com.jsa.api.service.FuncionarioService;

@RestController
@RequestMapping("funcionario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;

	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastraDadosFuncionario(@RequestBody FuncionarioForm form) {
		FuncionarioDTO f = this.funcionarioService.cadastraDadosFuncionario(form);
		return ResponseEntity.ok(f);
	}
	
	@GetMapping
	public ResponseEntity<?> listarFuncionarios() {
		List<FuncionarioDTO> listaFuncionario = this.funcionarioService.listaFuncionario();
		return ResponseEntity.ok(listaFuncionario);
	}
	
}
