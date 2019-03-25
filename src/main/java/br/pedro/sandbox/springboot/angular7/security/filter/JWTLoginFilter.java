package br.pedro.sandbox.springboot.angular7.security.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.pedro.sandbox.springboot.angular7.security.domain.Credentials;
import br.pedro.sandbox.springboot.angular7.security.domain.JwtConfig;
import br.pedro.sandbox.springboot.angular7.security.domain.User;
import br.pedro.sandbox.springboot.angular7.security.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter{

	private AuthenticationManager authManager;
	private final JwtConfig jwtConfig;
	
	public JWTLoginFilter(
			AuthenticationManager authManager, JwtConfig jwtConfig) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
		
		this.setRequiresAuthenticationRequestMatcher(
				new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response){
		
		try {
			Credentials creds = new ObjectMapper().readValue(
					request.getInputStream(), Credentials.class);
			UsernamePasswordAuthenticationToken authToken = 
					new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), Collections.emptyList());
			return authManager.authenticate(authToken);
		}catch(IOException e) {
			throw new RuntimeException("Invalid credentials.");
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		
		Long now = System.currentTimeMillis();
		String token = Jwts.builder()
				.setSubject(auth.getName())
				.claim("authorities", auth.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
				.compact();
		
		//Bearer <token>
		response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix().concat(token));
		response.setContentType("application/json");
		
		StringBuilder jsonResponse = new StringBuilder();
		jsonResponse
			.append("{")
				.append("\"accessToken\": \"Bearer ").append(token).append("\",")
				.append("\"username\" : \"").append(auth.getName()).append("\"")
			.append("}");
			
		response.getWriter().write(jsonResponse.toString());
		
	}
	

}
