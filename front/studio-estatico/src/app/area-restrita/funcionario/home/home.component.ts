import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { PacoteAtendimento } from 'src/app/shered/component/lista-procedimentos/pacote-atendimento';
import { MensagemService } from 'src/app/shered/component/mensagem/mensagem-service';
import { BotaBuilder } from 'src/app/shered/component/modal/bota.builder';
import { BotaoDTO } from 'src/app/shered/component/modal/botao-dto';
import { ModalService } from 'src/app/shered/component/modal/modal.service';
import { AtendimentoHome } from 'src/app/shered/model/atendimento-home';
import { AtendimentoDetalhe } from 'src/app/shered/model/atendimentoDetalhe';
import { ProcedimentoAtendimento } from 'src/app/shered/model/procedimento-atendimento';
import { AtendimentoClient } from 'src/app/shered/service/client/atendimento.client';

@Component({
    selector: 'home-app',
    templateUrl: 'home.component.html',
    styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit, OnDestroy {

    private $destroy = new Subject<boolean>();
    private diaSelecionado = new Date();
    private atendimentoSelecionado!:AtendimentoHome|AtendimentoDetalhe;
    private tipoAcao?: 'FINALIZA'|'DESMARCA';  
    listaAtendimentoDia = new Array<AtendimentoHome>();
    atendimentoDetalhe = new AtendimentoDetalhe();
    clienteDetalheForm = new FormGroup({});
    showDetalhe = false;
    isEdicaoHabilitado = false;
    textoModalConfirmacao = '';

    modalEventDetalheAtendimento = new Subject<'OPEN'|'CLOSE'>();
    modalEventListaProcedimento = new Subject<'OPEN'|'CLOSE'>();
    modalEventConfirmacao = new Subject<'OPEN'|'CLOSE'>();
    listaBotaoModalDetalheAtendimento = new Array<BotaoDTO>();
    listaBotoesModalProcedimento = new Array<BotaoDTO>();
    listaBotoesModalConfirmacao = new Array<BotaoDTO>();

    botaoFecharEventSubject = new Subject<boolean>();
    botaoFecharEventSubjectProcedimento = new Subject<boolean>();
    botaoSimConfirmacaoEventSubject = new Subject<boolean>();
    botaoNaoConfirmacaoEventSubject = new Subject<boolean>();
    botaoEditarAtendimentoEventSubject = new Subject<boolean>();
    botaoFinalizaAtendimentoEventSubject = new Subject<boolean>();
    botaoDesmarcarAtendimentoEventSubject = new Subject<boolean>();

    constructor(
        private atendimentoClient: AtendimentoClient,
        private mensagemService:MensagemService,
        private datePipe: DatePipe,
        private modalService:ModalService
    ) { }
    
    
    ngOnInit() {
        this.carregarAtendimentoDia(this.diaSelecionado);
        this.funcionalidadesBotaoAbrirAtendimento();
    }

    ngOnDestroy(): void {
        this.$destroy.next(true);
        this.$destroy.unsubscribe();
    }

    recebeDiaSelecionadoEvent(diaSelecionado: Date) {
        this.diaSelecionado = diaSelecionado;
        this.carregarAtendimentoDia(diaSelecionado);
    }

    abrirModalConfirmacao(tipoAcao: 'FINALIZA'|'DESMARCA' ,itemSelecionado:AtendimentoHome|AtendimentoDetalhe){
        this.tipoAcao = tipoAcao;
        this.atendimentoSelecionado = itemSelecionado;
        this.modalEventConfirmacao.next('OPEN');
        this.textoModalConfirmacao = `Deseja realmente ${tipoAcao.toLowerCase()}r o atendimento?`;
    }
    
    abriAtendimento(item:AtendimentoHome) {
        this.isEdicaoHabilitado = false;
        this.atendimentoClient
            .detalhaAtendimento(item.id)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next: (res:AtendimentoDetalhe) => {
                    this.atendimentoDetalhe = res;
                    this.clienteDetalheForm = this.montarClienteDetalheForm(res);
                    this.showDetalhe = true;
                },
                error: (err:HttpErrorResponse) => console.log(err),
            });

        this.modalEventDetalheAtendimento.next('OPEN');
    }

    removeProcedimento(p: ProcedimentoAtendimento) {
        if(this.atendimentoDetalhe.procedimentos.length <= 1) {
            this.mensagemService.warning("Atendimento não pode ficar sem procedimento.");
            return;
        }

        let index = this.atendimentoDetalhe.procedimentos.indexOf(p);
        this.atendimentoDetalhe.procedimentos.splice(index, 1);
    }

    adicionaProcedimento() {
        this.modalEventDetalheAtendimento.next('CLOSE');
        this.modalEventListaProcedimento.next('OPEN');
    }

    recebeProcedimentoSelecionado(pacoteAtendimento: PacoteAtendimento){
        // PacoteAtendimento
        this.modalEventListaProcedimento.next('CLOSE');
        this.modalEventDetalheAtendimento.next('OPEN');
        if(this.atendimentoDetalhe.funcionario.id !== pacoteAtendimento.funcionario.id){
            let nomeFunci = this.atendimentoDetalhe.funcionario.nome;
            this.mensagemService.warning(`Nesse atendimento pode somente incluir procedimento realizado pela(o) ${nomeFunci}.`);
            this.mensagemService.info('Para realizar um procedimento de funcionário diferente, favor incluir outro atendimento.');
            return;
        }
        let p = new ProcedimentoAtendimento().procedimentoToProcedimentoAtendimento(pacoteAtendimento.procedimento);
        
        this.atendimentoDetalhe.procedimentos.push(p);
        
    }
    
    finalizarAtendimento(atendimento:AtendimentoHome|AtendimentoDetalhe) {
        this.atendimentoClient
            .finalizarAtendimento(atendimento.id)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:() => {
                    this.mensagemService.success('Atendimento finalizado com sucesso');
                    this.carregarAtendimentoDia(this.diaSelecionado);
                }
            })
    }
    
    desmarcaAtendimento(atendimento:AtendimentoHome|AtendimentoDetalhe) {
        this.atendimentoClient
            .desmarcaAtendimento(atendimento.id)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:() => {
                    this.mensagemService.success('Atendimento Descarmado com sucesso');
                    this.carregarAtendimentoDia(this.diaSelecionado);
                }
            })
    }
    
    private efetivarConfirmacao() {
        this.modalEventConfirmacao.next('CLOSE');
        if(this.tipoAcao == 'FINALIZA')
            this.finalizarAtendimento(this.atendimentoSelecionado);
        else if(this.tipoAcao == 'DESMARCA')
            this.desmarcaAtendimento(this.atendimentoSelecionado);
        else 
            this.mensagemService.error('Tipo de ação não identificada, contacte o adimistrador do sistema');
    }
    
    private funcionalidadesBotaoAbrirAtendimento() {
        this.listaBotaoModalDetalheAtendimento = new BotaBuilder()
            .adicionaBota(new BotaoDTO('Finalizar', 'primary', this.botaoFinalizaAtendimentoEventSubject))
            .adicionaBota(new BotaoDTO('Desmarcar', 'primary', this.botaoDesmarcarAtendimentoEventSubject))
            .adicionaBota(new BotaoDTO('Editar', 'primary', this.botaoEditarAtendimentoEventSubject))
            .adicionaBota(new BotaoDTO('Fechar', 'secondary', this.botaoFecharEventSubject))
            .getListaBotao();

        this.modalService.acaoBota(this.botaoFecharEventSubject, () => this.modalEventDetalheAtendimento.next("CLOSE"));
        this.modalService.acaoBota(this.botaoEditarAtendimentoEventSubject, () => this.isEdicaoHabilitado = !this.isEdicaoHabilitado )
        this.modalService.acaoBota(this.botaoFinalizaAtendimentoEventSubject, () => {
            this.modalEventDetalheAtendimento.next('CLOSE');
            this.abrirModalConfirmacao('FINALIZA', this.atendimentoDetalhe)    
        });
        this.modalService.acaoBota(this.botaoDesmarcarAtendimentoEventSubject, () => {
            this.modalEventDetalheAtendimento.next('CLOSE');
            this.abrirModalConfirmacao('DESMARCA', this.atendimentoDetalhe);
        });

        this.listaBotoesModalProcedimento = new BotaBuilder()
            .adicionaBota(new BotaoDTO('Fechar', 'secondary', this.botaoFecharEventSubjectProcedimento))
            .getListaBotao();

        this.modalService.acaoBota(this.botaoFecharEventSubjectProcedimento, () => this.modalEventListaProcedimento.next("CLOSE"));

        this.listaBotoesModalConfirmacao = new BotaBuilder()
            .adicionaBota(new BotaoDTO('Sim', 'primary', this.botaoSimConfirmacaoEventSubject))
            .adicionaBota(new BotaoDTO('Não', 'secondary', this.botaoNaoConfirmacaoEventSubject))
            .getListaBotao();
        
        this.modalService.acaoBota(this.botaoSimConfirmacaoEventSubject, () => this.efetivarConfirmacao());
        this.modalService.acaoBota(this.botaoNaoConfirmacaoEventSubject, () => this.modalEventConfirmacao.next('CLOSE'));
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
    
    private montarClienteDetalheForm(a: AtendimentoDetalhe) {
        return new FormGroup({
            dataAtendimento: new FormControl(a.dataHoraAtendimento, Validators.required),
            fimAtendimento: new FormControl(this.datePipe.transform(a.dataHoraFimAtendimento, 'HH:mm'), Validators.required),
            desconto: new FormControl(a.desconto, Validators.required),
            cliente: new FormControl(a.cliente.nome, Validators.required),
            valor: new FormControl(a.valor, Validators.required),
            funcionario: new FormControl(a.funcionario.nome, Validators.required),
        });
    }
}