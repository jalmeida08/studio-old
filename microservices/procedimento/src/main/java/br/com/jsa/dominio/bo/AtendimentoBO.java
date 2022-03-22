package br.com.jsa.dominio.bo;

import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

import br.com.jsa.infra.model.Procedimento;

public class AtendimentoBO {

	public Double calculaValorAtendimento(List<Procedimento> listaProcedimentoAtendimento) {
		DoubleAdder somaProcedimento = new DoubleAdder();
		listaProcedimentoAtendimento
			.stream()
			.forEach(p-> somaProcedimento.add(p.getValor()));
		return somaProcedimento.doubleValue();
	}

	
}
