package br.com.jsa.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.VerificaIdFuncionarioDTO;
import br.com.jsa.api.form.FuncionarioForm;
import br.com.jsa.infra.exception.ParametroInvalidoException;
import br.com.jsa.infra.model.Funcionario;
import br.com.jsa.infra.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public Funcionario getFuncionario(String id) {
		return funcionarioRepository
		.findById(id)
		.orElseThrow(() -> new ParametroInvalidoException("O parâmetro informado não foi localizado na base de dados"));
	}
	
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

	public List<VerificaIdFuncionarioDTO> verificaFuncionario(List<String> listaIdFuncionario) {
		List<VerificaIdFuncionarioDTO> lstIds = new ArrayList<VerificaIdFuncionarioDTO>();
		
		listaIdFuncionario
			.stream()
			.forEach( f -> {
				if(this.funcionarioRepository.findById(f).isPresent())
					lstIds.add(new VerificaIdFuncionarioDTO(f, true));
				else
					lstIds.add(new VerificaIdFuncionarioDTO(f, false));
			});
		return lstIds;
	}

	public List<FuncionarioDTO> consultaListaFuncionario(List<String> listaFuncionarios) {
		List<FuncionarioDTO> listaDadosFuncionario = new ArrayList<FuncionarioDTO>();
		listaFuncionarios
			.stream()
			.forEach(f -> {
				listaDadosFuncionario.add(new FuncionarioDTO(getFuncionario(f)));
			});
		return listaDadosFuncionario;
	}

	public FuncionarioDTO consultaFuncionarioPorId(String id) {
		Funcionario f = getFuncionario(id);
		return new FuncionarioDTO(f);
	}
	
	
}
