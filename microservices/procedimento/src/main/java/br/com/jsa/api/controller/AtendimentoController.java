package br.com.jsa.api.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jsa.api.form.AtendimentoForm;
import br.com.jsa.api.service.AtendimentoService;

@RestController
@RequestMapping("atendimento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AtendimentoController {
	
	@Autowired
	private AtendimentoService atendimentoService;
	
	@PostMapping
	public ResponseEntity<?> adicionaAtendimento(@RequestBody AtendimentoForm atendimentoForm){
		atendimentoService.adicionaAtendimento(atendimentoForm);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/calcula-valor-atendimento")
	public ResponseEntity<?> calculaValorAtendimento(@RequestBody List<String> listaProcedimentos){
		final Double calculaValorAtendimento = atendimentoService.calculaValorAtendimento(listaProcedimentos);
		return ResponseEntity.ok(calculaValorAtendimento);
	}
	 

}
