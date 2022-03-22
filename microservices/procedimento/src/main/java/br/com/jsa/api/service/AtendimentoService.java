package br.com.jsa.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.api.client.ClienteClient;
import br.com.jsa.api.form.AtendimentoForm;
import br.com.jsa.dominio.bo.AtendimentoBO;
import br.com.jsa.infra.exception.ParametroInvalidaException;
import br.com.jsa.infra.model.Atendimento;
import br.com.jsa.infra.model.Procedimento;
import br.com.jsa.infra.repository.AtendimentoRepository;
import br.com.jsa.infra.repository.ProcedimentoRepository;

@Service
public class AtendimentoService {
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private ProcedimentoRepository procedimentoRepository;
	
	@Autowired
	private ClienteClient clienteClient;
	
	private Procedimento buscaProcedimentoDoAtendimento(String id) {
		return this.procedimentoRepository
			.findById(id)
			.orElseThrow(() -> new ParametroInvalidaException("Procedimento informado não foi encontrado na base de dados"));
	}

	private Double efetuarCalculoProcedimento(List<Procedimento> listaProcedimentoAtendimento) {
		AtendimentoBO bo = new AtendimentoBO();
		return bo.calculaValorAtendimento(listaProcedimentoAtendimento);
	}
	
	public void adicionaAtendimento(AtendimentoForm atendimentoForm) {
		Atendimento a = atendimentoForm.toAtendimento();
		final List<Procedimento> listaProcedimentoAtendimento = buscaListaProcedimentos(a.getProcedimentos());
		clienteClient.validaClientePorId(a.getIdCliente());
		
		a.setValor(efetuarCalculoProcedimento(listaProcedimentoAtendimento));
		atendimentoRepository.save(a);
	}


	private List<Procedimento> buscaListaProcedimentos(List<String> procedimentos) {
		List<Procedimento> listaProcedimento = new ArrayList<Procedimento>();
		procedimentos
			.stream()
			.forEach( p -> {
				listaProcedimento.add(buscaProcedimentoDoAtendimento(p));				
			});
		return listaProcedimento;
	}

	public Double calculaValorAtendimento(List<String> listaProcedimentos) {
		return efetuarCalculoProcedimento(buscaListaProcedimentos(listaProcedimentos));
	}
	
}
