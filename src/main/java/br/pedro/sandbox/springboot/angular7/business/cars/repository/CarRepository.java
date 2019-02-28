package br.pedro.sandbox.springboot.angular7.business.cars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.pedro.sandbox.springboot.angular7.business.cars.domain.Car;

@RepositoryRestResource
@CrossOrigin(origins="http://localhost:4200")
public interface CarRepository extends JpaRepository<Car, Long>{

}
