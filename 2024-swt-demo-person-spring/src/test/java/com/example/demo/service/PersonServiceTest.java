package com.example.demo.service;

import com.example.demo.domain.Person;
import com.example.demo.domain.PersonRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonServiceTest {

    @Mock
    PersonRepo personRepo;

    PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personService = new PersonService(personRepo);
    }

    @Test
    void liefert_angeforderte_person() throws Exception {
        Person person = new Person("Peter Lustig", "Bauwagen 1");
        when(personRepo.findById(1)).thenReturn(Optional.of(person));

        Person actualPerson = personService.findMyPerson(1);

        assertEquals(person.getName(), actualPerson.getName());
        assertEquals(person.getAddress(), actualPerson.getAddress());
        verify(personRepo).findById(1);
    }

    @Test
    void wirft_exception_wenn_person_nicht_vorhanden() {
        when(personRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.findMyPerson(1));

        verify(personRepo).findById(1);
    }
}