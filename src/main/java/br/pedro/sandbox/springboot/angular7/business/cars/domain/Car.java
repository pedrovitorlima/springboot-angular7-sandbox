package br.pedro.sandbox.springboot.angular7.business.cars.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="cars")
public class Car {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@Column(name="giphy_url")
	private String giphyUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGiphyUrl() {
		return giphyUrl;
	}

	public void setGiphyUrl(String giphyUrl) {
		this.giphyUrl = giphyUrl;
	}
}
