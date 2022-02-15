package br.com.jsa.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.FuncionarioForm;
import br.com.jsa.infra.model.Funcionario;
import br.com.jsa.infra.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public FuncionarioDTO cadastraDadosFuncionario(FuncionarioForm form) {
		Funcionario f = this.funcionarioRepository.save(form.toFuncionario());
		return new FuncionarioDTO(f);
	}
	
	public List<FuncionarioDTO> listaFuncionario(){
		return this.funcionarioRepository
				.findAll()
				.stream()
				.map(FuncionarioDTO::new)
				.collect(Collectors.toList());
	}
	
	
}
