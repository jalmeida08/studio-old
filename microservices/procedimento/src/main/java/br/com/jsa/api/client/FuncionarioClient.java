package br.com.jsa.api.client;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.VerificaIdFuncionarioDTO;

@FeignClient("cadastro-basico/funcionario")
public interface FuncionarioClient {

	@RequestMapping(
		method = RequestMethod.POST,
		produces = MediaType.APPLICATION_JSON,
		consumes = MediaType.APPLICATION_JSON,
		value = "/valida-funcionarios"
	)
	public List<VerificaIdFuncionarioDTO> idFuncionarioIsValid(List<String> listaIdFuncionario);

	
	@RequestMapping(
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON,
			consumes = MediaType.APPLICATION_JSON,
			value = "/consulta-lista"
		)
	public List<FuncionarioDTO> consultaDadosListaFuncionario(List<String> funcionarios);
	
}
