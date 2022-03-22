package br.com.jsa.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.api.dto.ClienteDTO;
import br.com.jsa.api.form.ClienteForm;
import br.com.jsa.infra.model.Cliente;
import br.com.jsa.infra.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	private Cliente getCliente(String id) {
		return clienteRepository
				.findById(id)
				.orElseThrow(() -> new ParametroInvalidoException("Cliente não foi localizado com o id informado"));
	}

	public ClienteDTO buscaClientePorId(String id) {
		return new ClienteDTO(getCliente(id));
	}
	
	public void salvaCliente(ClienteForm clienteForm) {
		this.clienteRepository.save(clienteForm.toCliente());
	}

	public List<ClienteDTO> listaCliente() {
		return clienteRepository
				.findAll()
				.stream()
				.map(ClienteDTO::new).collect(Collectors.toList());
	}
	
	
}
