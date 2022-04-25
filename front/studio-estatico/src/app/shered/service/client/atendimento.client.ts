import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Atendimento } from '../../model/atendimento';
import { ValidaAtendimento } from '../../model/valida-atendimento';

@Injectable({providedIn: 'root'})
export class AtendimentoClient {

    constructor(private http: HttpClient) {}

    listaAtendimentoDia(atendimentoDia:Date): Observable<Atendimento[]> {
        return this.http.post<Atendimento[]>(`${environment.baseApi.STUDIO}/atendimento/lista-atendimento-dia`, {atendimentoDia});
    }

    validaAtendimento(atendimento:Atendimento):Observable<ValidaAtendimento> {
        return this.http.post<ValidaAtendimento>(
            `${environment.baseApi.STUDIO}/atendimento/valida-atendimento`, atendimento);
    }
    
}