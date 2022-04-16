import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AreaPublicaModule } from './area-publica/area-publica.module';
import { AreaRestritaModule } from './area-restrita/area-restrita.module';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', loadChildren: () => AreaPublicaModule },
  { path: 'studio', loadChildren: () => AreaRestritaModule }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
