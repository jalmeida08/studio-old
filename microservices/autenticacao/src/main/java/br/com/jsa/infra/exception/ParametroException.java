package br.com.jsa.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParametroException extends RuntimeException {

	private static final long serialVersionUID = -7250884815342452054L;
	
	public ParametroException() {
		super("Parametro informado é inválido ou não foi localizado na base de dados");
	}

}
