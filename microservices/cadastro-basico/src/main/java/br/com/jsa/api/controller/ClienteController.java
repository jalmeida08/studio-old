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

import br.com.jsa.api.dto.ClienteDTO;
import br.com.jsa.api.form.ClienteForm;
import br.com.jsa.api.service.ClienteService;

@RestController
@RequestMapping("cliente")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> salvaCliente (@RequestBody ClienteForm clienteForm) {
		ClienteDTO c = clienteService.salvaCliente(clienteForm);
		return ResponseEntity.ok(c);
	}
	
	@GetMapping
	public ResponseEntity<?> listaCliente(){
		List<ClienteDTO> lstCliente = clienteService.listaCliente();
		return ResponseEntity.ok(lstCliente);
	}
	
	@GetMapping("/{idCliente}")
	public ResponseEntity<?> buscaClientePorId(@PathVariable("idCliente") String id){
		ClienteDTO dto = clienteService.buscaClientePorId(id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping("/busca-cliente")
	public ResponseEntity<?> buscaClientePorNome(@RequestBody ClienteForm clienteForm){
		List<ClienteDTO> lstCliente = clienteService.buscaCliente(clienteForm);
		return ResponseEntity.ok(lstCliente);
	}

	
}
