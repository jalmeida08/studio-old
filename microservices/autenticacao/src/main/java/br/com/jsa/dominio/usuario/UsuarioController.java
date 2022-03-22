package br.com.jsa.dominio.usuario;

import br.com.jsa.api.dto.AlteraSenhaUsuarioForm;
import br.com.jsa.api.dto.UsuarioForm;

public interface UsuarioController {
	
	void novoUsuario(UsuarioForm usuarioForm);
	
	void alteraSenha(AlteraSenhaUsuarioForm alteraSenhaUsuarioForm);
	
	
}
