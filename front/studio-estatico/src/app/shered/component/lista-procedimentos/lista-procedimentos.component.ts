import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { Atendimento } from '../../model/atendimento';
import { Funcionario } from '../../model/funcionario';
import { Procedimento } from '../../model/procedimento';
import { DadosFuncionarioClient } from '../../service/client/dados-funcionario.client';
import { ProcedimentoClient } from '../../service/client/procedimento.client';
import { PacoteAtendimento } from './pacote-atendimento';

@Component({
    selector: 'lista-procedimento-app',
    templateUrl: './lista-procedimentos.component.html',
    styleUrls: ['lista-procedimento.component.scss']
})

export class ListaProcedimentoComponent implements OnInit, OnDestroy {

    private $destroy = new Subject<boolean>();
    private procedimento = new Procedimento();
    private funcionario = new Funcionario();
    private listaProcedimentoSelecionado = new Array<Procedimento>();
    private pacoteAtendimento!: PacoteAtendimento;
    @Output() enviaProcedimentoSelecionadoEvent = new EventEmitter();
    listaProcedimento = new Array<Procedimento>();
    listaFuncionario = new Array<Funcionario>();

    constructor(
        private procedimentoClient: ProcedimentoClient,
        private dadosFuncionarioClient: DadosFuncionarioClient
    ) { }
    
    ngOnInit() {
        this.buscaListaProcedimento();
    }

    ngOnDestroy(): void {
        this.$destroy.next(true);
        this.$destroy.unsubscribe();
    }
    
    buscaDadosProfissional(procedimento:Procedimento){
        this.selecionaProcedimento(procedimento);
        this.dadosFuncionarioClient
            .consultaListaFuncionario(procedimento.funcionarios)
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next: (res:Funcionario[]) => this.listaFuncionario = res,
                error: (err:HttpErrorResponse) => console.error(err)
            });
    }
    
    private selecionaProcedimento(procedimento:Procedimento){
        this.procedimento = procedimento;
    }

    selecionaFuncionario(funcionario:Funcionario) {
        this.funcionario = funcionario;
    }
    
    addProcedimentoLista(){
        this.listaProcedimentoSelecionado.push(this.procedimento); 
        this.enviaProcedimento();
    }
    
    private enviaProcedimento(){
        let pacoteProcedimento = 
            new PacoteAtendimento(this.listaProcedimentoSelecionado, this.funcionario);
        this.enviaProcedimentoSelecionadoEvent.emit(pacoteProcedimento);
    }

    mostrarDuracao(tempo:number){
        if(tempo >= 60)
            return (tempo/60) + ' hrs';
        else return tempo + ' min.'
    }

    private buscaListaProcedimento() {
        this.procedimentoClient
            .listaProcedimentos()
            .pipe(takeUntil(this.$destroy))
            .subscribe({
                next:(res:Procedimento[]) => this.listaProcedimento = res,
                error:(err: HttpErrorResponse) => console.error(err)
            });
    }


}