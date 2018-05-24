package com.swi.study.backend.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SwaggerTestContoller {
	@RequestMapping(method = RequestMethod.GET, path = "/{id}", produces = "application/json")
	public Integer getPersonById(@PathVariable int id) {
		return 10000;
	}

}
