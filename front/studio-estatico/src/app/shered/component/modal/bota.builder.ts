import { BotaoDTO } from "./botao-dto";

export class BotaBuilder {
    private listaBotao = new Array<BotaoDTO>();

    adicionaBota(botao:BotaoDTO){
        this.listaBotao.push(botao);
        return this;
    }

    getListaBotao():Array<BotaoDTO> {
        return this.listaBotao;
    }
}