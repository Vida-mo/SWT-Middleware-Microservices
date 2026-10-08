package com.example.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer>{

    Optional<Person> findByNameAndAddress(String name, String address);

    Optional<Person> findByNameOrAddress(String name, String address);
}
