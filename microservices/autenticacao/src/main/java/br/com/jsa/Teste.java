package br.com.jsa;

import java.time.LocalDate;

public class Teste {
	
	public static void main(String[] args) {
		var anoMesDia = "20220316";
		final var ano = Integer.valueOf(anoMesDia.substring(0,4));
		final var mes = Integer.valueOf(anoMesDia.substring(5,6));
		final var dia = Integer.valueOf(anoMesDia.substring(7));
		System.out.println(LocalDate.of(ano, mes, dia));
		
		
	}

}
