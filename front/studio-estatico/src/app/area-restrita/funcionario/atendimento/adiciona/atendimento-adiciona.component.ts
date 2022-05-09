import { HttpErrorResponse, HttpResponseBase } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { PacoteAtendimento } from 'src/app/shered/component/lista-procedimentos/pacote-atendimento';
import { MensagemService } from 'src/app/shered/component/mensagem/mensagem-service';
import { BotaoDTO } from 'src/app/shered/component/modal/botao-dto';
import { AgendaDiaFuncionario } from 'src/app/shered/model/agenda-dia-funcionario';
import { Atendimento } from 'src/app/shered/model/atendimento';
import { Cliente } from 'src/app/shered/model/cliente';
import { EstadoAtendimento } from 'src/app/shered/model/enum/estado-atendimento';
import { Funcionario } from 'src/app/shered/model/funcionario';
import { Procedimento } from 'src/app/shered/model/procedimento';
import { ValidaAtendimento } from 'src/app/shered/model/valida-atendimento';
import { AtendimentoClient } from 'src/app/shered/service/client/atendimento.client';
import { ClienteClient } from 'src/app/shered/service/client/cliente.client';
import { AtendimentoForm } from './atendimento-form';

declare const $: any;

@Component({
    templateUrl: 'atendimento-adiciona.component.html',
    styleUrls: ['./atendimento-adiciona.component.scss']
})
export class AtendimentoNovoComponent implements OnInit, OnDestroy {

    private $destroy = new Subject<boolean>();
    // VARIAVEIS PARA ABRIR/FECHAR MODAL
    modalEventBuscaCliente = new Subject<'OPEN'|'CLOSE'>();
    modalEventAdicionaCliente = new Subject<'OPEN'|'CLOSE'>();
    modalEventAdicionaProcedimento = new Subject<'OPEN'|'CLOSE'>();
    modalEventConfirmacaoAtendimento = new Subject<'OPEN'|'CLOSE'>();
    // SUBJECTS PARA TRATAR EVENTOS E LISTA DE BOTOES DE MODAIS
    botaoSalvarEventSubject = new Subject<boolean>();
    botaoFecharEventSubject = new Subject<boolean>();
    listaBotaoModalConfirmacao = new Array<BotaoDTO>();
    listaBotaoUnicoFechar = new Array<BotaoDTO>();

    atendimentoForm = new AtendimentoForm();
    atendimentoValidado = new ValidaAtendimento();
    listaIdProcedimento = new Array<string>();
    listaAtendimentoDiaPesquisado = new Array<Atendimento>();
    listaCliente = new Array<Cliente>();
    formAtendimento = new FormGroup({});
    formCliente = new FormGroup({});
    cliente?: Cliente;
    listaPocedimentoSelecionado = new Array<Procedimento>();
    funcionarioAtendimento = new Funcionario();
    isShowBuscaCliente = false;
    isShowNovoCliente = false;
    monstrarSelect = false;
    
    constructor(
        private clienteClient: ClienteClient,
        private atendimentoClient: AtendimentoClient,
        private mensagemService:MensagemService
        ) { }

    ngOnInit() {
        this.formAtendimento = this.montaFormAtendimento();
        this.montaBotaoComum(this.listaBotaoUnicoFechar);
    }

    ngOnDestroy(): void {
        this.$destroy.next(true);
        this.$destroy.unsubscribe();
    }
    
    recebeDadosFormulario(cliente:Cliente) {        
        if(this.isShowBuscaCliente)
            this.buscaCliente(cliente);
        else 
            this.adicionaNovoCliente(cliente);
    }

    exibirTelaNovoCliente():void {
        this.acaoBotao(this.botaoFecharEventSubject, () => this.modalEventAdicionaCliente.next('CLOSE'));
        this.modalEventAdicionaCliente.next('OPEN');
        this.formCliente = this.atualizaNovoClienteFormulario();
        this.isShowNovoCliente = true;
        this.isShowBuscaCliente = false;
    }

    exibirTelaBuscaCliente():void {
        this.acaoBotao(this.botaoFecharEventSubject ,() => this.modalEventBuscaCliente.next('CLOSE'));
        this.formCliente = this.atualizaBuscaClienteFormulario();
        this.modalEventBuscaCliente.next('OPEN');
        this.isShowNovoCliente = false;
        this.isShowBuscaCliente = true;
    }
    
    exibirTelaProcedimento() {        
        this.acaoBotao(this.botaoFecharEventSubject ,() =>this.modalEventAdicionaProcedimento.next('CLOSE'));
        this.modalEventAdicionaProcedimento.next('OPEN');
    }

    selecionaCliente(cliente:Cliente):void {
        this.cliente = cliente;
        this.atendimentoForm.idCliente = cliente.id;
        this.modalEventBuscaCliente.next('CLOSE');
    }

    recebeProcedimentoSelecionado(pacoteAtendimento:PacoteAtendimento) {
        this.modalEventAdicionaProcedimento.next('CLOSE');
        
        if(this.validaInclusaoDeUmNovoProcedimento(pacoteAtendimento)) {
            this.listaPocedimentoSelecionado.push(pacoteAtendimento.procedimento);
            this.funcionarioAtendimento = pacoteAtendimento.funcionario;
            this.atendimentoForm.idFuncionario = pacoteAtendimento.funcionario.id;
            
            this.listaIdProcedimento.push(pacoteAtendimento.procedimento.id);
        }
    }
    
    mostrarDuracao(tempo:number){
        if(tempo >= 60)
            return (tempo/60) + ' hrs';
        else return tempo + ' min.'
    }

    excluirAtendimento() {
        this.atendimentoForm = new AtendimentoForm();
        this.cliente = new Cliente();
        this.listaPocedimentoSelecionado = new Array<Procedimento>();
        this.funcionarioAtendimento = new Funcionario();
        this.listaAtendimentoDiaPesquisado = new Array<Atendimento>();
    }
    
    buscaAgenda() {
        if(this.atendimentoForm.idFuncionario.length <= 0 || this.formAtendimento.controls['dataAtendimento'].value === null){
            this.mensagemService.info('Selecione um procedimento e uma data para poder buscar a agenda do profissional');
            return;
        }
        let agendaDiaFunci = new AgendaDiaFuncionario();
        agendaDiaFunci.dia = this.formAtendimento.controls['dataAtendimento'].value;
        agendaDiaFunci.id = this.atendimentoForm.idFuncionario;
        this.atendimentoClient
            .buscaAgendaDiaFuncionario(agendaDiaFunci)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:(res:Atendimento[]) => {
                    this.listaAtendimentoDiaPesquisado = res;
                },
                error:(err:HttpErrorResponse) => console.error(err)                
            });
    }
    
    validaAtendimento() {
        if(!this.formAtendimento.valid || this.cliente === undefined || this.listaPocedimentoSelecionado.length === 0){
            this.mensagemService.warning('Fomulário inválido');
            return;
        }

        this.listaBotaoModalConfirmacao = new Array<BotaoDTO>();
        this.listaBotaoModalConfirmacao.push(new BotaoDTO('Salvar', 'primary', this.botaoSalvarEventSubject));
        this.montaBotaoComum(this.listaBotaoModalConfirmacao);
        let atendimento = new AtendimentoForm();        
        let hora = this.formAtendimento.controls['horaAtendimento'].value;
        let data =  this.formAtendimento.controls['dataAtendimento'].value;
        
        atendimento.dataHoraAtendimento = data+"T"+hora;
        atendimento.estadoAtendimento = EstadoAtendimento.AGENDADO;
        atendimento.idCliente = this.cliente.id;
        atendimento.procedimentos = this.listaIdProcedimento;
        atendimento.idFuncionario = this.funcionarioAtendimento.id;
        this.aguarRepostaModalConfirmacao(atendimento);
        this.atendimentoClient
            .validaAtendimento(atendimento)
            .pipe(takeUntil(this.$destroy))
                .subscribe({
                    next:(res:ValidaAtendimento) => {
                        this.atendimentoValidado = res;
                        this.modalEventConfirmacaoAtendimento.next('OPEN');
                    },
                    error:(err:HttpErrorResponse) => this.mensagemService.error(err.error.message)
                });
        
    }

    private buscaCliente(cliente: Cliente) {
        this.clienteClient
        .buscaCliente(cliente)
        .pipe(takeUntil(this.$destroy))
        .subscribe({
            next: (res:Cliente[]) => this.listaCliente = res,
            error: (err:HttpErrorResponse) => console.error(err)
        });
    }
    
    private validaInclusaoDeUmNovoProcedimento(pacoteAtendimento:PacoteAtendimento): boolean {
        let formAtendimento = this.formAtendimento.value as Atendimento;
        
        if(this.atendimentoForm.idFuncionario !== undefined && this.atendimentoForm.idFuncionario !== pacoteAtendimento.funcionario.id && 
            (formAtendimento.dataHoraAtendimento === null || formAtendimento.estadoAtendimento === 0)
        ){
            this.mensagemService.info('Para incluir um procedimento de um funcionario diferente é necessário criar outro atendimento.');
            return false;
        }
        return true;
    }

    private adicionaNovoCliente(cliente:Cliente){
        this.modalEventAdicionaCliente.next('CLOSE');
        this.clienteClient
            .novoCliente(cliente)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next: (res:Cliente) =>{
                    this.formCliente = this.atualizaNovoClienteFormulario();
                    this.cliente = res;
                }, 
                error: (err:HttpErrorResponse) => console.error(err)
            });
    }
    
    private salvarDadosAntedimento(atendimento:AtendimentoForm){
        this.modalEventConfirmacaoAtendimento.next('CLOSE');
        this.atendimentoClient
            .salvarAtendimento(atendimento)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next: () =>{
                    this.mensagemService.success('Atendimento registrado com sucesso!!!');
                }, 
                error: (err:HttpErrorResponse) =>{
                    this.mensagemService.error(err.error.message)
                }
            });
    }
    
    private aguarRepostaModalConfirmacao(atendimentoFoorm:AtendimentoForm){
        this.acaoBotao(this.botaoFecharEventSubject,
            () => this.modalEventConfirmacaoAtendimento.next('CLOSE'));
        
        this.acaoBotao(this.botaoSalvarEventSubject,
            () => this.salvarDadosAntedimento(atendimentoFoorm));
    }
    
    private atualizaNovoClienteFormulario():FormGroup {
        return new FormGroup({
            nome: new FormControl('', Validators.required),
            dataNascimento: new FormControl('', Validators.required)
        });
    }

    private atualizaBuscaClienteFormulario():FormGroup {
        return new FormGroup({
            nome: new FormControl(''),
            dataNascimento: new FormControl('')
        });
    }

    private montaFormAtendimento():FormGroup {
        return new FormGroup({
            estadoAtendimento: new FormControl(EstadoAtendimento.AGENDADO.valueOf(),Validators.required),
            dataAtendimento: new FormControl(null,Validators.required),
            horaAtendimento: new FormControl('',Validators.required),
        });
    }

    
    private montaBotaoComum(listaBota:Array<BotaoDTO>){
        listaBota.push(new BotaoDTO('Fechar', 'secondary', this.botaoFecharEventSubject));
    }
    
    private acaoBotao(botaEvent:Subject<any>, funcaoCallBack:any) {
        botaEvent
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:(res) => {
                    if(res)
                        funcaoCallBack();
                }
            });
    }

    
}