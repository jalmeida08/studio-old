package br.com.jsa.api.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.jsa.infra.model.Atendimento;
import br.com.jsa.infra.model.EstadoAtendimento;
import br.com.jsa.infra.model.Procedimento;

public class AtendimentoForm {

	private Float desconto;
	private LocalDateTime dataAgendamento;
	private EstadoAtendimento estadoAtendimento;
	private List<String> procedimentos = new ArrayList<String>();
	
	public Atendimento toAtendimento() {
		Atendimento a = new Atendimento();
		a.setDesconto(desconto);
		a.setDataAgendamento(dataAgendamento);
		a.setEstadoAtendimento(estadoAtendimento);
		return a;
	}

	public void setDesconto(Float desconto) {
		this.desconto = desconto;
	}

	public void setDataAgendamento(LocalDateTime dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public void setEstadoAtendimento(EstadoAtendimento estadoAtendimento) {
		this.estadoAtendimento = estadoAtendimento;
	}

	public void setProcedimentos(List<String> procedimentos) {
		this.procedimentos = procedimentos;
	}
	
	
}
