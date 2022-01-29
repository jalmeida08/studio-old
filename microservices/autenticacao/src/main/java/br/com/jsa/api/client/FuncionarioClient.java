package br.com.jsa.api.client;

import javax.ws.rs.core.MediaType;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.jsa.api.dto.FuncionarioDTO;
import br.com.jsa.api.dto.FuncionarioForm;
import feign.Headers;

@FeignClient("cadastro-basico")
public interface FuncionarioClient {
	
	@RequestMapping(
		method = RequestMethod.POST,
		produces = MediaType.APPLICATION_JSON,
		consumes = MediaType.APPLICATION_JSON,
		value = "/funcionario"
	)
	@Headers("Accept: application/json")
	public FuncionarioDTO cadastraDadosFuncionario(FuncionarioForm form);

}
