package br.pedro.sandbox.springboot.angular7.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SecurityController {

	@PostMapping("/")
	public String sayHi() {
		return "Hi";
	}
}
