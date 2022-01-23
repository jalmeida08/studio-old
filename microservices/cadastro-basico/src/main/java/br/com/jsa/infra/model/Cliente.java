package br.com.jsa.infra.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cliente")
public class Cliente extends Pessoa{
	
	@Id
	private String id;

	public String getId() {
		return id;
	}
	
	
}
