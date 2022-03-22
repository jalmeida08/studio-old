package br.com.jsa.dominio.usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.jsa.api.dto.AlteraSenhaUsuarioForm;

public class AlteraSenhaBO {
	
	private AlteraSenhaUsuarioForm alteraSenhaForm;
	private final String senhaSalva;
	
	public AlteraSenhaBO(AlteraSenhaUsuarioForm alteraSenhaForm, String senhaSalva) {
		this.alteraSenhaForm = alteraSenhaForm;
		this.senhaSalva = senhaSalva;
		verificaSenha();
	}
	
	private void verificaSenha() {
		final boolean isEquals;
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		isEquals = encoder.matches(alteraSenhaForm.getSenhaAntiga(), this.senhaSalva);
		if(!isEquals) {
			throw new RuntimeException("A senha informada não é compatível com a senha salva");
		}
	}
}
