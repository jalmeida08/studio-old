package br.com.jsa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.FuncionarioForm;
import br.com.jsa.api.service.FuncionarioService;

@RestController
@RequestMapping("funcionario")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;

	@GetMapping
	public ResponseEntity<?> cadastraDadosFuncionario(FuncionarioForm form) {
		FuncionarioDTO f = this.funcionarioService.cadastraDadosFuncionario(form);
		return ResponseEntity.ok(f);
	}
	
}
