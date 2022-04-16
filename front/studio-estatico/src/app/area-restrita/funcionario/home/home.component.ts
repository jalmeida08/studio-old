import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, take, takeUntil } from 'rxjs';
import { Atendimento } from 'src/app/shered/model/atendimento';
import { AtendimentoService } from 'src/app/shered/service/atendimento.service';

@Component({
    selector: 'home-app',
    templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit, OnDestroy {

    private destroy$ = new Subject<boolean>();
    constructor(
        private atendimentoService: AtendimentoService
    ) { }
    
    
    ngOnInit() {
        console.log("CHAMANDO...");        
        this.carregarAtendimentoDia();
    }
    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.unsubscribe();
    }
    
    carregarAtendimentoDia() {
        console.log("tuuuu...");
        this.atendimentoService
            .listaAtendimentoDia(new Date())
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next:(res:Atendimento[]) => console.log(res),
                error:(err: HttpErrorResponse) => console.error(err)
                
            })
    }
}