import { NgModule } from '@angular/core';
import { CalendarioModule } from 'src/app/shered/component/calendario/calendario.module';

import { HomeComponent } from './home.component';

@NgModule({
    imports: [
        CalendarioModule
    ],
    exports: [],
    declarations: [HomeComponent],
    providers: [],
})
export class HomeModule { }
