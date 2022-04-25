import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, take, takeUntil } from 'rxjs';
import { Atendimento } from 'src/app/shered/model/atendimento';
import { AtendimentoClient } from 'src/app/shered/service/client/atendimento.client';

@Component({
    selector: 'home-app',
    templateUrl: 'home.component.html',
    styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit, OnDestroy {

    private destroy$ = new Subject<boolean>();
    constructor(
        private atendimentoClient: AtendimentoClient
    ) { }
    
    
    ngOnInit() {
        this.carregarAtendimentoDia(new Date());
    }
    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.unsubscribe();
    }

    recebeDiaSelecionadoEvent(diaSelecionado: Date) {
        this.carregarAtendimentoDia(diaSelecionado);        
    }

    private carregarAtendimentoDia(data:Date) {
        this.atendimentoClient
            .listaAtendimentoDia(data)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next:(res:Atendimento[]) => console.log(res),
                error:(err: HttpErrorResponse) => console.error(err)
            });
    }
}