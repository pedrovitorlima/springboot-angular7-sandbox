package br.pedro.sandbox.springboot.angular7.business.cars.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.pedro.sandbox.springboot.angular7.SpringBootAngular7Application;
import br.pedro.sandbox.springboot.angular7.business.cars.domain.Car;
import br.pedro.sandbox.springboot.angular7.business.cars.repository.CarRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootAngular7Application.class)
public class CarRepositoryTest {

	@Autowired
	CarRepository repository;
	
	@Test
	public void shouldSaveAValidCar() {
		Car car = new Car();
		car.setId(1L);
		car.setName("car-test");
		
		Car save = repository.save(car);
		assertThat(save).isNotNull();
	}
}
