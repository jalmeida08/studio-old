import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AreaRestritaRoutingModule } from './area-restrita-routing.module';
import { FuncionarioModule } from './funcionario/funcionario.module';


@NgModule({
    imports: [
        FuncionarioModule,
        AreaRestritaRoutingModule
    ],
    exports: [],
    declarations: [],
    providers: [],
})
export class AreaRestritaModule { }
