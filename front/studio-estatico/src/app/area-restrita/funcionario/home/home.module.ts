import { CommonModule, DatePipe } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CalendarioModule } from 'src/app/shered/component/calendario/calendario.module';
import { ListaProcedimentoModule } from 'src/app/shered/component/lista-procedimentos/lista-procedimentos.module';
import { ModalModule } from 'src/app/shered/component/modal/modal.module';

import { HomeComponent } from './home.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule,
        
        CalendarioModule,
        ModalModule,
        ListaProcedimentoModule
    ],
    exports: [],
    declarations: [HomeComponent],
    providers: [DatePipe],
})
export class HomeModule { }
