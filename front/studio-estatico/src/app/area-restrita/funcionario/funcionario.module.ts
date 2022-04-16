import { NgModule } from '@angular/core';
import { FuncionarioRoutingModule } from './funcionario-routing.module';
import { HomeModule } from './home/home.module';


@NgModule({
    declarations: [],
    imports: [
        HomeModule,
        FuncionarioRoutingModule
    ],
    exports: [],
    providers: [],
})
export class FuncionarioModule { }
