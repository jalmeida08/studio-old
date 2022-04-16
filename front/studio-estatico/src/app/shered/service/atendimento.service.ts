import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Atendimento } from '../model/atendimento';

@Injectable({providedIn: 'root'})
export class AtendimentoService {

    constructor(private http: HttpClient) {}

    listaAtendimentoDia(atendimentoDia:Date): Observable<Atendimento[]> {
        return this.http.post<Atendimento[]>(`${environment.baseApi.STUDIO}/atendimento/lista-atendimento-dia`, {atendimentoDia});
    }
    
}