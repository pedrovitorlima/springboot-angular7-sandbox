package br.pedro.sandbox.springboot.angular7.security.domain;

public class Credentials {

	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public Credentials setUsername(String username) {
		this.username = username;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public Credentials setPassword(String password) {
		this.password = password;
		return this;
	}
}
