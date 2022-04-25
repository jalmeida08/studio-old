package br.com.jsa.infra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.jsa.infra.model.Cliente;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String>{
	
	public List<Cliente> findByNomeLikeIgnoreCase(String nome);
	public List<Cliente> findByDataNascimento(LocalDate DataNascimento);
	public List<Cliente> findByDataNascimentoAndNomeLikeIgnoreCase(String nome, LocalDate DataNascimento);
	

}
