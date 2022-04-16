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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jsa.api.dto.AcessoDTO;
import br.com.jsa.api.form.AcessoForm;
import br.com.jsa.api.service.AcessoService;

@RestController
@RequestMapping("/acesso")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;

	@PostMapping
	@Transactional
	public ResponseEntity<?> adicionaAcesso(@RequestBody AcessoForm acessoForm) {
		acessoService.adicionaAcesso(acessoForm.toAcesso());
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority({'ADMIN'})")  
	public ResponseEntity<?> listaAcesso() {
		List<AcessoDTO> dto = acessoService.listaAcesso();
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> listaAcesso(@PathVariable("id") String id) {
		AcessoDTO dto = acessoService.buscaAcessoPorId(id);
		return ResponseEntity.ok(dto);
	}

}
