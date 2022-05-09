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

import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.VerificaIdFuncionarioDTO;
import br.com.jsa.api.form.FuncionarioForm;
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
	
	@PostMapping("/valida-funcionarios")
	public ResponseEntity<?> validaFuncionarios(@RequestBody List<String> listaIdFuncionario) {
		List<VerificaIdFuncionarioDTO> verificaFunciDTO =
				this.funcionarioService.verificaFuncionario(listaIdFuncionario);
		return ResponseEntity.ok(verificaFunciDTO);
	}
	
	@GetMapping("/{idFuncionario}")
	public ResponseEntity<?> buscaFuncionarioPorId(@PathVariable("idFuncionario") String id) {
		FuncionarioDTO f = funcionarioService.consultaFuncionarioPorId(id);
		return ResponseEntity.ok(f);
		
	}
	
	@PostMapping("/consulta-lista")
	public ResponseEntity<?> consultaDadosListaFuncionario(@RequestBody List<String> listaFuncionarios){
		List<FuncionarioDTO> listaFuncionario =
				funcionarioService.consultaListaFuncionario(listaFuncionarios);
		return ResponseEntity.ok(listaFuncionario);
	}
}
