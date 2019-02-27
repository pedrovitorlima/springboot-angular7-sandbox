package br.pedro.sandbox.springboot.angular7.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.pedro.sandbox.springboot.angular7.security.domain.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTTokenuthenticationFilter extends OncePerRequestFilter{

	private final JwtConfig jwtConfig;
	
	public JWTTokenuthenticationFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(jwtConfig.getHeader());
		
		//Validate invalid token header
		if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = header.replace(jwtConfig.getPrefix(), "");
		
		try {
			//Here we validate the token. Claims is like a map of parameters
			Claims claims = Jwts.parser()
					.setSigningKey(jwtConfig.getSecret().getBytes())
					.parseClaimsJws(token)
					.getBody();
			
			String username = claims.getSubject();
			if (username != null) {
				List<String> authorities = (List<String>) claims.get("authorities");
				
				//Authentication, indeed...
				//1) getting the token
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						username, 
						null, 
						authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
				//2) saving the valid token on our context to future requests verifications
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}catch(Exception e) {
			SecurityContextHolder.clearContext();
		}
		
		filterChain.doFilter(request, response);
	}


}
