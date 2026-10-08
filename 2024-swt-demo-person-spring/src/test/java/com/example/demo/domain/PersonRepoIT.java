package com.example.demo.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonRepoIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepo personRepo;

    @Test
    void findet_person() {
        Person person = new Person("Peter Lustig", "Bauwagen 1");
        entityManager.persist(person);
        entityManager.flush();

        Optional<Person> actualPerson = personRepo.findById(1);

        assertTrue(actualPerson.isPresent());
        assertEquals("Peter Lustig", actualPerson.get().getName());
        assertEquals("Bauwagen 1", actualPerson.get().getAddress());
    }

    @Test
    void findet_keine_person() {
        Optional<Person> actualPerson = personRepo.findById(1);

        assertFalse(actualPerson.isPresent());
    }

    @Test
    void spring_data_jpa_magic() {
        Person person = new Person("Peter Lustig", "Bauwagen 1");
        entityManager.persist(person);
        entityManager.flush();

        Optional<Person> actualPerson = personRepo.findByNameAndAddress("Peter Lustig", "Bauwagen 1");

        assertTrue(actualPerson.isPresent());
        assertEquals("Peter Lustig", actualPerson.get().getName());
        assertEquals("Bauwagen 1", actualPerson.get().getAddress());
    }

    @Test
    void spring_data_jpa_magic_2() {
        Person person = new Person("Peter Lustig", "Bauwagen 1");
        entityManager.persist(person);
        entityManager.flush();

        Optional<Person> actualPerson = personRepo.findByNameAndAddress("Fritz Fuchs", "Bauwagen 1");

        assertFalse(actualPerson.isPresent());
    }

    @Test
    void spring_data_jpa_magic_3() {
        Person person = new Person("Peter Lustig", "Bauwagen 1");
        entityManager.persist(person);
        entityManager.flush();

        Optional<Person> actualPerson = personRepo.findByNameOrAddress("Fritz Fuchs", "Bauwagen 1");

        assertTrue(actualPerson.isPresent());
        assertEquals("Peter Lustig", actualPerson.get().getName());
        assertEquals("Bauwagen 1", actualPerson.get().getAddress());
    }

}