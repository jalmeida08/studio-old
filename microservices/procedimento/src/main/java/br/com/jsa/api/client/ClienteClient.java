package br.com.jsa.api.client;

import javax.ws.rs.core.MediaType;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("/cadastro-basico/cliente")
public interface ClienteClient {
	
	@RequestMapping(
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON,
			consumes = MediaType.APPLICATION_JSON,
			value = "/valida-cliente"
		)
	public void validaClientePorId(String id);

}
