package br.com.jsa.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jsa.api.client.ClienteClient;
import br.com.jsa.api.dto.AtendimentoDTO;
import br.com.jsa.api.dto.BuscaDadosAgendaFuncionarioDTO;
import br.com.jsa.api.dto.ClienteDTO;
import br.com.jsa.api.dto.ValidaInclusaoAtendimentoDTO;
import br.com.jsa.api.form.AtendimentoForm;
import br.com.jsa.dominio.bo.AtendimentoBO;
import br.com.jsa.infra.exception.NegocioException;
import br.com.jsa.infra.exception.ParametroInvalidaException;
import br.com.jsa.infra.model.Atendimento;
import br.com.jsa.infra.model.EstadoAtendimento;
import br.com.jsa.infra.model.Procedimento;
import br.com.jsa.infra.repository.AtendimentoRepository;
import br.com.jsa.infra.repository.ProcedimentoRepository;
import br.com.jsa.util.DateUtil;

@Service
public class AtendimentoService {
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private ProcedimentoRepository procedimentoRepository;
	
	@Autowired
	private ClienteClient clienteClient;
	
	private Atendimento getAtendimento(String id) {
		return atendimentoRepository
				.findById(id)
				.orElseThrow(() -> new ParametroInvalidaException("Procedimento informado não foi encontrado na base de dados"));
	}
	
	private Procedimento buscaProcedimentoDoAtendimento(String id) {
		return this.procedimentoRepository
			.findById(id)
			.orElseThrow(() -> new ParametroInvalidaException("Procedimento informado não foi encontrado na base de dados"));
	}

	private Double efetuarCalculoProcedimento(List<Procedimento> listaProcedimentoAtendimento) {
		return new AtendimentoBO()
				.calculaValorAtendimento(listaProcedimentoAtendimento);
	}
	
	private List<Procedimento> buscaListaProcedimentos(List<String> procedimentos) {
		var listaProcedimento = new ArrayList<Procedimento>();
		procedimentos
			.forEach( p -> listaProcedimento.add(buscaProcedimentoDoAtendimento(p)));
		return listaProcedimento;
	}
	
//	private List<Atendimento> buscaAtendimentoPeriodo(
//			LocalDateTime dataHora1, LocalDateTime dataHora2, String estadoAtendimento){
//		
//		return atendimentoRepository.findByDataHoraAtendimentoBetweenAndEstadoAtendimento(dataHora1, dataHora2, estadoAtendimento); 
//	}
	private List<Atendimento> buscaAtendimentoPeriodoFuncionario(
			LocalDateTime dataHora1, LocalDateTime dataHora2, String estadoAtendimento, String idFuncionario){
		return 
				atendimentoRepository
						.findByDataHoraAtendimentoBetweenAndIdFuncionarioAndEstadoAtendimento(
								dataHora1, dataHora2, idFuncionario, estadoAtendimento);
	}
	
	public ValidaInclusaoAtendimentoDTO validaNovoAtendimento(AtendimentoForm atendimentoForm) {
			final var listaProcedimentoAtendimento = buscaListaProcedimentos(atendimentoForm.getProcedimentos());
			var dataFimProcedimento =
					new AtendimentoBO()
					.calcularDataFimProcedimento(atendimentoForm.getDataHoraAtendimento(), listaProcedimentoAtendimento);
			
			return new ValidaInclusaoAtendimentoDTO(
					consultaSepossuiAtendimentosConflitantes(atendimentoForm.getIdFuncionario(), atendimentoForm.getDataHoraAtendimento(), dataFimProcedimento)
					.stream()
					.map(AtendimentoDTO::new)
					.collect(Collectors.toList()), atendimentoForm.toAtendimento());
	}
	
	public void adicionaAtendimento(AtendimentoForm atendimentoForm) {
			Atendimento a = atendimentoForm.toAtendimento();
			final var listaProcedimentoAtendimento = buscaListaProcedimentos(a.getProcedimentos());
			
			var dataFimProcedimento = new AtendimentoBO()
					.calcularDataFimProcedimento(a.getDataHoraAtendimento(), listaProcedimentoAtendimento);
			
			a.setDataHoraFimAtendimento(dataFimProcedimento);
			
			var listaAtendimentoConflitantes = 
					consultaSepossuiAtendimentosConflitantes(
							atendimentoForm.getIdFuncionario(), a.getDataHoraAtendimento(), a.getDataHoraFimAtendimento());
			
			if(!listaAtendimentoConflitantes.isEmpty())
				throw new NegocioException("O horário informado colide com outros atendimentos");
			
			clienteClient.validaClientePorId(a.getIdCliente());
			a.setValor(efetuarCalculoProcedimento(listaProcedimentoAtendimento));
			atendimentoRepository.save(a);
	}

	private List<Atendimento> consultaSepossuiAtendimentosConflitantes(
			String idFuncionario,
			LocalDateTime dataHoraAtendimento, LocalDateTime dataHoraFimAtendimento){
		
		var bo = new AtendimentoBO();
		var dataHoraInicioBusca = dataHoraAtendimento.truncatedTo(ChronoUnit.DAYS);
		var dataHoraFimBusca = dataHoraAtendimento.plusMinutes(240);
		var listaAtendimentoPeriodo = buscaAtendimentoPeriodoFuncionario(
								dataHoraInicioBusca, dataHoraFimBusca, EstadoAtendimento.AGENDADO.name(), idFuncionario);
		
		return bo.listarAtendimentosNoMesmoHorario(listaAtendimentoPeriodo, dataHoraAtendimento, dataHoraFimAtendimento);
	}

	public Double calculaValorAtendimento(List<String> listaProcedimentos) {
		return efetuarCalculoProcedimento(buscaListaProcedimentos(listaProcedimentos));
	}

	public ClienteDTO consultarDadosCliente(String id) {
		return clienteClient.buscaClientePorId(id);
	}

	public List<AtendimentoDTO> listaAtendimento() {
		return atendimentoRepository
				.findAll()
				.stream()
				.map(AtendimentoDTO::new)
				.collect(Collectors.toList());
	}

	public AtendimentoDTO desmarcaAtendimento(String id) {
		Atendimento a = getAtendimento(id);
		a.setEstadoAtendimento(EstadoAtendimento.DESMARCADO);
		atendimentoRepository.save(a);
		return new AtendimentoDTO(a);
	}

	public void finalizaAtendimento(String id) {
		Atendimento a = getAtendimento(id);
		a.setEstadoAtendimento(EstadoAtendimento.FINALIZADO);
		atendimentoRepository.save(a);
	}

	public List<AtendimentoDTO> listaAtendimentoMesAnoInformado(String anoMes) {
		final LocalDateTime dataInicio;
		final LocalDateTime dataFim;
		if(anoMes.isEmpty()) {
			dataInicio = DateUtil.retornaDataInicio("");
			dataFim = DateUtil.retornaDataFim("");
		} else {
			dataInicio = DateUtil.retornaDataInicio(anoMes);
			dataFim = DateUtil.retornaDataFim(anoMes);
		}
		return atendimentoRepository
			.findByDataHoraAtendimentoBetweenAndEstadoAtendimento(
					dataInicio, dataFim, EstadoAtendimento.AGENDADO.name())
			.stream()
			.map(AtendimentoDTO::new)
			.collect(Collectors.toList());
	}
	
	public List<AtendimentoDTO> listaAtendimentoDiaInformado(LocalDate dataInformada) {
		final var dataInicio = dataInformada.atTime(LocalTime.MIN);
		final var dataFim = dataInformada.atTime(LocalTime.MAX);
		return atendimentoRepository
			.findByDataHoraAtendimentoBetweenAndEstadoAtendimento(
					dataInicio, dataFim, EstadoAtendimento.AGENDADO.name())
			.stream()
			.map(AtendimentoDTO::new)
			.collect(Collectors.toList());
	}

	public List<AtendimentoDTO> buscaAgendaFuncionarioDia(BuscaDadosAgendaFuncionarioDTO buscaAgenda) {
		var dataHoraInicio = LocalDateTime.of(buscaAgenda.getDia(), LocalTime.MIN);
		var dataHoraFim = LocalDateTime.of(buscaAgenda.getDia(), LocalTime.MAX);
		
		return atendimentoRepository
			.findByDataHoraAtendimentoBetweenAndIdFuncionarioAndEstadoAtendimento(
					dataHoraInicio, dataHoraFim, buscaAgenda.getId(), EstadoAtendimento.AGENDADO.name())
			.stream()
			.map(AtendimentoDTO::new)
			.collect(Collectors.toList());
		
	}
	
}
