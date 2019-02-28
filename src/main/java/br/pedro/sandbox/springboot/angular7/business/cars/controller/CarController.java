package br.pedro.sandbox.springboot.angular7.business.cars.controller;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pedro.sandbox.springboot.angular7.business.cars.domain.Car;
import br.pedro.sandbox.springboot.angular7.business.cars.repository.CarRepository;

@RestController
public class CarController {

	@Autowired
	private CarRepository carRepository;
	
	@GetMapping("/cool-cars")
	@CrossOrigin(origins = "http://localhost:4200")
	public Collection<Car> coolCars() {
		return carRepository.findAll().stream()
				.filter(this::isCool)
				.collect(Collectors.toList());
	}

	public boolean isCool(Car car) {
		return !car.getName().equals("Uno") && 
				!car.getName().equals("Gol") && 
				!car.getName().equals("Classic") && 
				!car.getName().equals("Veloster");
	}
	
}
