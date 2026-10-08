package com.example.demo.boundary;

import com.example.demo.domain.Person;
import com.example.demo.service.PersonNotFoundException;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        super();
        this.personService = personService;
    }


    @RequestMapping(value = "/person/{personId}", method = {RequestMethod.GET})
//	@GetMapping("person")
    public ResponseEntity<Person> personMethod(@PathVariable int personId) {
        try {
            Person person = personService.findMyPerson(personId);
            return ResponseEntity.ok().body(person);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
