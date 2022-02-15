package br.com.jsa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jsa.api.dto.TokenDTO;
import br.com.jsa.api.dto.UsuarioDTO;
import br.com.jsa.api.form.LoginForm;
import br.com.jsa.infra.conf.TokenService;
import br.com.jsa.infra.model.Usuario;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
		try {
			Authentication authenticate =
					this.authenticationManager.authenticate(loginForm.toConverter());
			String token = this.tokenService.gerarToken(authenticate);
			return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
		} catch (AuthenticationException e) {
			throw new RuntimeException("Usuario ou senha invalido");
		}
	}
	
	@GetMapping("/userInfo")
	public UsuarioDTO userInfo(@AuthenticationPrincipal Usuario usuarioLogado) {
		return new UsuarioDTO(usuarioLogado);
	}
}
