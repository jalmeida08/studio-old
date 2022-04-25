package br.com.jsa;

import java.util.ArrayList;
import java.util.List;

public class Teste {
	
	public static void main(String[] args) {
		var lst = new ArrayList<String>();
		var a = List.of("teste");
		var b = List.of("teste 2");
		var c = List.of("teste 3");
		var d = List.of("teste 4");
		lst.addAll(a);
		lst.addAll(b);
		lst.addAll(c);
		lst.addAll(d);
		System.out.println(lst);
		
		
	}

}
