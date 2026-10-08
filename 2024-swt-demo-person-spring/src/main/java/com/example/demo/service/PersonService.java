package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.Person;
import com.example.demo.domain.PersonRepo;

import java.util.Optional;

@Service
public class PersonService {
	private final PersonRepo personRepo;

	public PersonService(PersonRepo personRepo) {
		super();
		this.personRepo = personRepo;
	}
	
	public Person findMyPerson(Integer id) throws PersonNotFoundException {
		Optional<Person> personOptional = personRepo.findById(id);

		return personOptional.orElseThrow(PersonNotFoundException::new);
	}
		
}
