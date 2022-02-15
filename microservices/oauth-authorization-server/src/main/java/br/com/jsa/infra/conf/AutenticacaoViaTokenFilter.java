package br.com.jsa.infra.conf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.jsa.api.service.UsuarioService;
import br.com.jsa.infra.model.Usuario;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{

	private TokenService tokenService;
	
	private UsuarioService usuarioService;

	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioService usuarioService) {
		this.tokenService = tokenService;
		this.usuarioService = usuarioService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(request);

		if(token != null) {
			boolean valido = tokenService.isTokenValid(token);
			
			if(valido)
				autenticarCliente(token);
			
		}
		filterChain.doFilter(request, response);
	}

	private void autenticarCliente(String token) {
		String idUsuario = this.tokenService.getIdUsuario(token);
		Usuario u = usuarioService.buscarPorId(idUsuario); 
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer "))
			return null;
		return token.substring(7, token.length());
	}

}
