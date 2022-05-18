import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as moment from 'moment';
import { Subject, takeUntil } from 'rxjs';
import { PacoteAtendimento } from 'src/app/shered/component/lista-procedimentos/pacote-atendimento';
import { MensagemService } from 'src/app/shered/component/mensagem/mensagem-service';
import { BotaoBuilder } from 'src/app/shered/component/modal/botao.builder';
import { BotaoDTO } from 'src/app/shered/component/modal/botao-dto';
import { ModalService } from 'src/app/shered/component/modal/modal.service';
import { AtendimentoHome } from 'src/app/shered/model/atendimento-home';
import { AtendimentoDetalhe } from 'src/app/shered/model/atendimentoDetalhe';
import { EditaAtendimentoForm } from 'src/app/shered/model/edita-atendimento-form';
import { ProcedimentoAtendimento } from 'src/app/shered/model/procedimento-atendimento';
import { ValidaAtendimento } from 'src/app/shered/model/valida-atendimento';
import { AtendimentoClient } from 'src/app/shered/service/client/atendimento.client';

@Component({
    selector: 'home-app',
    templateUrl: 'home.component.html',
    styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit, OnDestroy {

    private $destroy = new Subject<boolean>();
    private diaSelecionado = new Date();
    private tipoAcao?: 'FINALIZAR'|'DESMARCAR'|'EDITAR';  
    private atendimentoEditado!: EditaAtendimentoForm;
    
    atendimentoSelecionado!:AtendimentoHome|AtendimentoDetalhe|EditaAtendimentoForm;
    listaAtendimentoDia = new Array<AtendimentoHome>();
    atendimentoDetalhe = new AtendimentoDetalhe();
    // atendimentoDetalhe = new AtendimentoDetalhe();
    clienteDetalheForm?: FormGroup;
    atendimentoEdicaoForm?: FormGroup;
    atendimentoValidado = new ValidaAtendimento();
    
    textoModalConfirmacao = '';

    inicializarProcedimentoSubject = new Subject<boolean>();
    modalEventListaProcedimento = new Subject<'OPEN'|'CLOSE'>();
    modalEventConfirmacao = new Subject<'OPEN'|'CLOSE'>();
    modalEventEdicaoAtendimento = new Subject<'OPEN'|'CLOSE'>();
    modalEventAlertaConflitoEdicao = new Subject<'OPEN'|'CLOSE'>();
    
    listaBotoesModalProcedimento = new Array<BotaoDTO>();
    listaBotoesModalConfirmacao = new Array<BotaoDTO>();
    listaBotoesModalEdicao = new Array<BotaoDTO>();
    listaBotoesModalAlertaConflitoEdicao = new Array<BotaoDTO>();

    botaoFecharProcedimentoEventSubject = new Subject<boolean>();
    botaoFecharEdicaoEventSubject = new Subject<boolean>();
    botaoSimConfirmacaoEventSubject = new Subject<boolean>();
    botaoNaoConfirmacaoEventSubject = new Subject<boolean>();
    botaoSalvarEdicaoAtendimentoEventSubject = new Subject<boolean>();
    botaoEstouCienteAtendimentoConflitanteEventSubject = new Subject<boolean>();
    botaoVoltarAtendimentoConflitanteEventSubject = new Subject<boolean>();

    constructor(
        private atendimentoClient: AtendimentoClient,
        private mensagemService:MensagemService,
        private datePipe: DatePipe,
        private modalService:ModalService
    ) { }
    
    
    ngOnInit() {
        this.carregarAtendimentoDia(this.diaSelecionado);
        this.funcionalidadesBotaoHome();
    }

    ngOnDestroy(): void {
        this.$destroy.next(true);
        this.$destroy.unsubscribe();
    }

    recebeDiaSelecionadoEvent(diaSelecionado: Date) {
        this.diaSelecionado = diaSelecionado;
        this.carregarAtendimentoDia(diaSelecionado);
    }

    abrirModalConfirmacao(tipoAcao: 'FINALIZAR'|'DESMARCAR'|'EDITAR' ,itemSelecionado:AtendimentoHome|AtendimentoDetalhe|EditaAtendimentoForm){
        this.tipoAcao = tipoAcao;
        this.atendimentoSelecionado = itemSelecionado;
        this.modalEventConfirmacao.next('OPEN');
        this.textoModalConfirmacao = `Deseja realmente ${tipoAcao.toLowerCase()} o atendimento?`;
    }
    
    removeProcedimento(p: ProcedimentoAtendimento) {
        if(this.atendimentoDetalhe.procedimentos.length <= 1) {
            this.mensagemService.warning("Atendimento não pode ficar sem procedimento.");
            return;
        }

        let index = this.atendimentoDetalhe.procedimentos.indexOf(p);
        this.atendimentoDetalhe.procedimentos.splice(index, 1);
        this.efetuaCalculoAtendimento();
    }

    adicionaProcedimento() {
        this.inicializarProcedimentoSubject.next(true);
        this.modalEventEdicaoAtendimento.next('CLOSE');
        this.modalEventListaProcedimento.next('OPEN');
    }

    recebeProcedimentoSelecionado(pacoteAtendimento: PacoteAtendimento){
        // PacoteAtendimento
        this.modalEventListaProcedimento.next('CLOSE');
        this.modalEventEdicaoAtendimento.next('OPEN');
        if(this.atendimentoDetalhe.funcionario.id !== pacoteAtendimento.funcionario.id){
            let nomeFunci = this.atendimentoDetalhe.funcionario.nome;
            this.mensagemService.warning(`Nesse atendimento pode somente incluir procedimento realizado pela(o) ${nomeFunci}.`);
            this.mensagemService.info('Para realizar um procedimento de funcionário diferente, favor incluir outro atendimento.');
            return;
        }
        let p = new ProcedimentoAtendimento().procedimentoToProcedimentoAtendimento(pacoteAtendimento.procedimento);
        this.atendimentoDetalhe.procedimentos.push(p);
        this.efetuaCalculoAtendimento();        
    }

    private efetuaCalculoAtendimento() {
        let valor = 0;
        let tempo = this.atendimentoDetalhe.dataHoraAtendimento;
        this.atendimentoDetalhe
            .procedimentos
            .forEach((item) => {
                valor += item.valor;
                tempo = moment(tempo).add(item.tempoDuracao, 'minute').toDate();
        });
        this.clienteDetalheForm?.controls['valor'].setValue(valor);
        this.clienteDetalheForm?.controls['fimAtendimento'].setValue(this.datePipe.transform(tempo, 'HH:mm'));
    }
    
    // FINALIZAR ATENDIMENTO
    finalizarAtendimento(atendimento:AtendimentoHome|AtendimentoDetalhe|EditaAtendimentoForm) {
        this.atendimentoClient
            .finalizarAtendimento(atendimento.id)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:() => {
                    this.mensagemService.success('Atendimento finalizado com sucesso');
                    this.carregarAtendimentoDia(this.diaSelecionado);
                }
            });
    }
    
    // DESMARCAR ATENDIMENTO
    desmarcaAtendimento(atendimento:AtendimentoHome|AtendimentoDetalhe|EditaAtendimentoForm) {
        this.atendimentoClient
            .desmarcaAtendimento(atendimento.id)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:() => {
                    this.mensagemService.success('Atendimento Descarmado com sucesso');
                    this.carregarAtendimentoDia(this.diaSelecionado);
                }
            });
    }
    
    selecionarAtendimento(atendimentoSelecionado:AtendimentoHome) {
        this.atendimentoSelecionado = atendimentoSelecionado;
        this.abriAtendimento(atendimentoSelecionado);
    }   
    
    habilitarEdicao(atendimentoSelecionado:AtendimentoHome) {
        this.tipoAcao = 'EDITAR';
        this.modalEventEdicaoAtendimento.next('OPEN');
        this.abriAtendimento(atendimentoSelecionado);
    }
    
    private abriAtendimento(item:AtendimentoHome) {
        this.atendimentoClient
            .detalhaAtendimento(item.id)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next: (res:AtendimentoDetalhe) => {
                    this.atendimentoDetalhe = res;
                    console.log(this.tipoAcao);
                    if(this.tipoAcao === 'EDITAR')
                        this.atendimentoEdicaoForm = this.montarClienteDetalheForm(res);
                    else
                        this.clienteDetalheForm = this.montarClienteDetalheForm(res);
                },
                error: (err:HttpErrorResponse) => console.log(err),
            });
    }
    
    private efetivarConfirmacao() {
        this.modalEventConfirmacao.next('CLOSE');
        if(this.tipoAcao === 'FINALIZAR')
            this.finalizarAtendimento(this.atendimentoSelecionado);
        else if(this.tipoAcao === 'DESMARCAR')
            this.desmarcaAtendimento(this.atendimentoSelecionado);
        else if(this.tipoAcao === 'EDITAR')
            this.salvaEdicaoAtendimento();
        else 
            this.mensagemService.error('Tipo de ação não identificada, contacte o adimistrador do sistema');
    }
    
    private salvaEdicaoAtendimento() {
        this.atendimentoClient
            .editaAtendimento(this.atendimentoEditado)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next: (res:ValidaAtendimento) => {
                    this.mensagemService.success('Atendimento editado com sucesso.')
                    this.modalEventConfirmacao.next('CLOSE');
                    this.modalEventAlertaConflitoEdicao.next('CLOSE');
                    this.carregarAtendimentoDia(this.diaSelecionado);
                },
                error:(err:HttpErrorResponse) => console.log(err)
                 
            });
    }
    
    private funcionalidadesBotaoHome() {
        this.montarBotoesEAcoesModalProcedimento();
        this.montarBotoesEAcaoesModalConfirmacao();
        this.montarBotoesEAcoesModalEdicao();
        this.montarBotoesEAcoesModalConflitoEdicao();
    }
    
    private carregarAtendimentoDia(data:Date) {
        if(data === null || data === undefined){
            this.mensagemService.warning('Selecione um dia para pesquisar');
            return;
        }
        let d = this.datePipe.transform(data, 'yyyy-MM-dd')
        this.atendimentoClient
            .listaAtendimentoDia(d?.toString())
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:(res:AtendimentoHome[]) => {
                    this.listaAtendimentoDia = res;
                },
                error:(err: HttpErrorResponse) => console.error(err)
            });
    }

    private validaEdicao() {
        let id = this.atendimentoDetalhe.id;
        let desconto = this.clienteDetalheForm?.controls['desconto'].value;
        let dataHoraAtendimento = this.clienteDetalheForm?.controls['dataHoraAtendimento'].value;
        let lstProcedimentos =new Array<string>();
        this.atendimentoDetalhe
            .procedimentos
            .forEach((i) => lstProcedimentos.push(i.id));
        this.atendimentoEditado = new EditaAtendimentoForm(id, desconto, dataHoraAtendimento, lstProcedimentos);

        this.atendimentoClient
            .validaEdicaoAtendimento(this.atendimentoEditado)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next: (res:ValidaAtendimento) => {
                    this.atendimentoValidado = res;
                    if(res.listaAtendimentoConflitante.length > 0){
                        this.modalEventAlertaConflitoEdicao.next('OPEN');
                        this.modalEventEdicaoAtendimento.next('CLOSE');
                    }else{
                        this.modalEventEdicaoAtendimento.next('CLOSE');
                        this.abrirModalConfirmacao('EDITAR', this.atendimentoEditado);
                    }
                },
                error:(err:HttpErrorResponse) => console.log(err)
                 
            });
    }
    
    private montarClienteDetalheForm(a: AtendimentoDetalhe) {
        return new FormGroup({
            dataHoraAtendimento: new FormControl(a.dataHoraAtendimento, Validators.required),
            fimAtendimento: new FormControl(this.datePipe.transform(a.dataHoraFimAtendimento, 'HH:mm'), Validators.required),
            desconto: new FormControl(a.desconto, Validators.required),
            cliente: new FormControl(a.cliente.nome, Validators.required),
            valor: new FormControl(a.valor, Validators.required),
            funcionario: new FormControl(a.funcionario.nome, Validators.required),
        });
    }

    // BOTÕES E AÇÕES TELA PROCEDIMENTO
    private montarBotoesEAcoesModalProcedimento() {
        this.listaBotoesModalProcedimento = new BotaoBuilder()
            .adicionaBota('Fechar', 'secondary', this.botaoFecharProcedimentoEventSubject)
            .getListaBotao();

        this.modalService.acaoBotao(this.botaoFecharProcedimentoEventSubject, () => this.modalEventListaProcedimento.next("CLOSE"));
    }

    // BOTÕES E AÇÕES TELA CONFIRMAÇÃO
    private montarBotoesEAcaoesModalConfirmacao() {
        this.listaBotoesModalConfirmacao = new BotaoBuilder()
            .adicionaBota('Sim', 'primary', this.botaoSimConfirmacaoEventSubject)
            .adicionaBota('Não', 'secondary', this.botaoNaoConfirmacaoEventSubject)
            .getListaBotao();
        
        this.modalService.acaoBotao(this.botaoSimConfirmacaoEventSubject, () => this.efetivarConfirmacao());
        this.modalService.acaoBotao(this.botaoNaoConfirmacaoEventSubject, () => this.modalEventConfirmacao.next('CLOSE'));
    }

    // BOTÕES E AÇÕES TELA EDIÇÃO
    private montarBotoesEAcoesModalEdicao() {
        this.listaBotoesModalEdicao = new BotaoBuilder()
            .adicionaBota('Salvar', 'primary', this.botaoSalvarEdicaoAtendimentoEventSubject)
            .adicionaBota('Voltar', 'secondary', this.botaoFecharEdicaoEventSubject)
            .getListaBotao();
        this.modalService.acaoBotao(this.botaoSalvarEdicaoAtendimentoEventSubject, () => this.validaEdicao())
        this.modalService.acaoBotao(this.botaoFecharEdicaoEventSubject, () => {
            this.modalEventEdicaoAtendimento.next('CLOSE');
        });
    }

    private montarBotoesEAcoesModalConflitoEdicao() {
        this.listaBotoesModalAlertaConflitoEdicao = new BotaoBuilder()
            .adicionaBota('Estou ciente e confirmo', 'primary', this.botaoEstouCienteAtendimentoConflitanteEventSubject)
            .adicionaBota('Voltar', 'secondary', this.botaoVoltarAtendimentoConflitanteEventSubject)
            .getListaBotao();
        this.modalService.acaoBotao(this.botaoEstouCienteAtendimentoConflitanteEventSubject, () => this.efetivarConfirmacao())
        this.modalService.acaoBotao(this.botaoVoltarAtendimentoConflitanteEventSubject, () => {
            this.modalEventEdicaoAtendimento.next('OPEN');
            this.modalEventAlertaConflitoEdicao.next('CLOSE');
        });
    }
    
}