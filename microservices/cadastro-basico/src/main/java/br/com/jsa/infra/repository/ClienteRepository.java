package br.com.jsa.infra.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.jsa.infra.model.Cliente;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String>{

}
