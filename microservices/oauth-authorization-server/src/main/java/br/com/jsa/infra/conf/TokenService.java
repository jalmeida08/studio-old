package br.com.jsa.infra.conf;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.jsa.infra.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Service
public class TokenService {
	
	@Value("${minhaaplicacap.jwt.expiration}")
	private String expiration;
	
	@Value("${minhaaplicacap.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authenticate) {
		Usuario usuario = (Usuario) authenticate.getPrincipal();
		Date now = new Date();
		Date dataExpiracao = new Date(now.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("MICRO SERVICO STUDIOS")
				.setSubject(usuario.getId())
				.setIssuedAt(now)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return false;			
		}
	}

	public String getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	
}
