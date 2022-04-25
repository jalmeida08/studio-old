import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { PacoteAtendimento } from 'src/app/shered/component/lista-procedimentos/pacote-atendimento';
import { Atendimento } from 'src/app/shered/model/atendimento';
import { Cliente } from 'src/app/shered/model/cliente';
import { EstadoAtendimento } from 'src/app/shered/model/enum/estado-atendimento';
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
    private atendimento = new Atendimento();
    atendimentoForm = new AtendimentoForm();
    listaAtendimento = new Array<string>();
    formCliente = new FormGroup({});
    cliente?: Cliente;
    formAtendimento = new FormGroup({});
    listaCliente = new Array<Cliente>();
    pacoteAtendimento?: PacoteAtendimento;
    isShowBuscaCliente = false;
    isShowNovoCliente = false;
    monstrarSelect = false;
    
    constructor(
        private clienteClient: ClienteClient,
        private atendimentoClient: AtendimentoClient
        ) { }

    ngOnInit() {
        this.formAtendimento = this.montarFormAtendimento();
        this.montaListaAtendimento();
    }

    ngOnDestroy(): void {
        this.$destroy.next(true);
        this.$destroy.unsubscribe();
    }
    
    recebeDadosFormulario(cliente:Cliente) {
        console.log(cliente);
        
        if(this.isShowBuscaCliente)
            this.buscaCliente(cliente);
        else 
            this.adicionaNovoCliente(cliente);
    }

    exibirTelaNovoCliente():void {
        this.formCliente = this.atualizaNovoClienteFormulario();
        this.isShowNovoCliente = true;
        this.isShowBuscaCliente = false;
    }

    exibirTelaBuscaCliente():void {
        this.formCliente = this.atualizaBuscaClienteFormulario();
        $('#buscaPorCliente').modal('show');
        this.isShowNovoCliente = false;
        this.isShowBuscaCliente = true;
    }
    
    selecionaCliente(cliente:Cliente):void {
        this.cliente = cliente;
        this.atendimento.idCliente = cliente.id;
        $('#buscaPorCliente').modal('hide');
    }

    recebeProcedimentoSelecionado(pacoteAtendimento:PacoteAtendimento) {
        this.pacoteAtendimento = pacoteAtendimento;
        this.atendimento.idFuncionario = pacoteAtendimento.funcionario.id;
        pacoteAtendimento
            .listaProcedimento.forEach((item) => {
                this.atendimento.procedimentos.push(item.id);
            });
    }
    
    salva() {
        let atendimento = this.formAtendimento.value as Atendimento;        
        this.atendimento.dataHoraAtendimento = atendimento.dataHoraAtendimento;
        this.atendimento.estadoAtendimento = atendimento.estadoAtendimento;
        this.atendimentoClient
            .validaAtendimento(this.atendimento)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:(res:ValidaAtendimento) => console.log(res),
                error:(err:HttpErrorResponse) => console.error(err)                
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
    
    private adicionaNovoCliente(cliente:Cliente){
        this.clienteClient
            .novoCliente(cliente)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next: () => this.formCliente = this.atualizaNovoClienteFormulario(),
                error: (err:HttpErrorResponse) => console.error(err)
            });
    }
    
    private montaListaAtendimento() {
        this.listaAtendimento.push('AGENDADO');
        this.listaAtendimento.push('DESMARCADO');
        this.listaAtendimento.push('FINALIZADO');
        this.monstrarSelect = true;
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

    private montarFormAtendimento():FormGroup {
        return new FormGroup({
            estadoAtendimento: new FormControl(EstadoAtendimento.AGENDADO.valueOf(),Validators.required),
            dataHoraAtendimento: new FormControl(new Date(),Validators.required),
            
        });
    }
}