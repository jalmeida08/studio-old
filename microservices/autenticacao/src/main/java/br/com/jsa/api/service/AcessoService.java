package br.com.jsa.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.api.dto.AcessoDTO;
import br.com.jsa.infra.exception.ParametroException;
import br.com.jsa.infra.model.Acesso;
import br.com.jsa.infra.repository.AcessoRepository;

@Service
public class AcessoService {

	@Autowired
	private AcessoRepository acessoRepository;
	
	private Acesso getAcesso(String id) {
		return acessoRepository
				.findById(id)
				.orElseThrow(() -> new ParametroException());
	}
	
	public void adicionaAcesso(Acesso a) {
		acessoRepository.save(a);
	}
	
	public List<AcessoDTO> listaAcesso(){
		return acessoRepository
				.findAll()
				.stream()
				.map(AcessoDTO::new)
				.collect(Collectors.toList());
	}

	public AcessoDTO buscaAcessoPorId(String id) {
		return new AcessoDTO(getAcesso(id));
	}
}
