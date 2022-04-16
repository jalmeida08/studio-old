import { EstadoAtendimento } from "./enum/estado-atendimento";

export class Atendimento {
    id!:string;
    idCliente! :string;
    valor!: string;
    desconto!:number;
    dataHoraAtendimento!: Date;
    dataHoraFimAtendimento!:Date;
    estadoAtendimento!: EstadoAtendimento;
    procedimentos!: Array<string>;
}