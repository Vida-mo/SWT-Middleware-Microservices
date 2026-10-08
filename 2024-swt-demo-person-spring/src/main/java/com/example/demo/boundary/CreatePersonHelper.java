package com.example.demo.boundary;

import com.example.demo.domain.Person;
import com.example.demo.domain.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Diese Klasse enthaelt einen Architektur-Verstoss
    Das ist nicht zu empfehlen und ist nur aus Vereinfachungszwecken in diesem Beispiel.
 */
@RestController
@RequestMapping("/persons")
public class CreatePersonHelper {

    private final PersonRepo personRepo;

    @Autowired
    public CreatePersonHelper(PersonRepo personRepo) {
        super();
        this.personRepo = personRepo;
    }


    @PostMapping(value = "/person")
    public ResponseEntity<?> personMethod() {
        personRepo.save(new Person("Tanja Testperson", "Tulpenweg 15"));
        return ResponseEntity.ok().build();
    }

}