package br.pedro.sandbox.springboot.angular7.security.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.pedro.sandbox.springboot.angular7.SpringBootAngular7Application;
import br.pedro.sandbox.springboot.angular7.security.domain.Credentials;

/**
 * For testing our security layer, we need a lot of distinct components on Spring Context, like UserDetailServiceImpl, UserRepository, etc.
 * Because of this we cant test only our weblayer using @WebMvcTest. 
 * With @SpringBootTest we are saying that our SprongBootAngular7Application.class will be used to load our context.
 * @autor Pedro Rodrigues
 * **/

/*We cant do this...*/
//@RunWith(SpringRunner.class)
//@WebMvcTest(SecurityController.class) //This loads only one controller in web layer. Only @Controller components will be loaded

/*But this works fine...*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootAngular7Application.class)
@WebAppConfiguration
public class LoginAuthTest {
	
	@Autowired
	private WebApplicationContext applicationContext;
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	private MockMvc mvc;
	
	private ObjectMapper json;
	
	@Before
	public void loadContexts() {
		json = new ObjectMapper();
		mvc = MockMvcBuilders.webAppContextSetup(applicationContext).addFilter(springSecurityFilterChain).build();
	}
	
	@Test
	public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
		final String baseUrl = "/";
		mvc.perform( post(baseUrl).contentType(MediaType.APPLICATION_JSON) ).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void shouldNotAllowAccessToInvalidUsers() throws Exception {
		Credentials credential = new Credentials()
				.setUsername("invalid_user")
				.setPassword("invalid_password");
		
		mvc.perform( post("/auth/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.writeValueAsString(credential)))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void allowAccessToValidUsers() throws Exception {
		Credentials credential = new Credentials()
				.setUsername("bruce.dickinson")
				.setPassword("bruce.dickinson");
		
		mvc.perform( post("/auth/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.writeValueAsString(credential)))
			.andExpect(status().is2xxSuccessful());
	}
	
	public void shouldAllowValidTokens() throws Exception {
		Credentials credential = new Credentials()
				.setUsername("bruce.dickinson")
				.setPassword("bruce.dickinson");
		
		MvcResult result = mvc.perform( post("/auth/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.writeValueAsString(credential)))
				.andReturn();
		
		String token = result.getResponse().getHeader("Authorization");
		assertThat(token).isNotNull();
		assertThat(token).isNotEmpty();
		
		mvc.perform( post("/")
				.header("Authorization", token))
		.andExpect(status().is2xxSuccessful());		
	}
	
	
}
