package br.pedro.sandbox.springboot.angular7.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.pedro.sandbox.springboot.angular7.security.domain.JwtConfig;
import br.pedro.sandbox.springboot.angular7.security.filter.JWTLoginFilter;
import br.pedro.sandbox.springboot.angular7.security.filter.JWTTokenuthenticationFilter;
import br.pedro.sandbox.springboot.angular7.security.service.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private UserDetailServiceImpl userDetailsServiceImpl;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//				.exceptionHandling()
//				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
//				//This filter will process new logins
//				.addFilter(new JWTLoginFilter(authenticationManager(), jwtConfig))
//				.authorizeRequests()
//					//This will configure free access to the authentication URL
//					.antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
//				.and()
//					.authorizeRequests()
//					.antMatchers("/console/**")
//					.permitAll()
//				.and()
//				//This filter will process requests and validate tokens in Authorization access
//					.addFilterAfter(authenticationTokenFilterBean(), 
//							UsernamePasswordAuthenticationFilter.class);
////				.authorizeRequests().anyRequest().authenticated();
		
		http.csrf().disable().authorizeRequests()
			.antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
			.anyRequest().authenticated()
			.and()
			
			.addFilterBefore(new JWTLoginFilter(authenticationManager(), jwtConfig), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JWTTokenuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class);
        
		http.headers().frameOptions().disable();

	}
	
	@Bean
    public JWTTokenuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JWTTokenuthenticationFilter(jwtConfig);
    }
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder());
    }

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}
}
