package br.pedro.sandbox.springboot.angular7.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.pedro.sandbox.springboot.angular7.security.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
	public User findByLogin(String login);
}
