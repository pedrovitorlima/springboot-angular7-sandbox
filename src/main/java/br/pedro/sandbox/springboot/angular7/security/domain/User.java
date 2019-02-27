package br.pedro.sandbox.springboot.angular7.security.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.UniqueElements;

@Entity
public class User {

	@Id
	@GeneratedValue
	private Integer id;
	
	@UniqueElements
	private String login;
	
	private String password;

	public User() {
		
	}
	
	public User(int id, Date birthDate, String name) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
}
