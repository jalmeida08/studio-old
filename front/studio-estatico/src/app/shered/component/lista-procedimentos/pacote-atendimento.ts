import { Funcionario } from "../../model/funcionario";
import { Procedimento } from "../../model/procedimento";

export class PacoteAtendimento {

    listaProcedimento = new Array<Procedimento>();
    funcionario = new Funcionario();

    constructor(lstProcedimento:Procedimento[], funcionario:Funcionario){
        this.funcionario = funcionario;
        this.listaProcedimento = lstProcedimento;
    }
}