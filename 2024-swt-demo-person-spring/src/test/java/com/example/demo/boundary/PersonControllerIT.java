package com.example.demo.boundary;

import com.example.demo.domain.Person;
import com.example.demo.service.PersonNotFoundException;
import com.example.demo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PersonController.class)
class PersonControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void liefert_200_mit_person() throws Exception {
        given(personService.findMyPerson(1)).willReturn(new Person("Peter Lustig", "Bauwagen 1"));

        MvcResult mvcResult = mockMvc.perform(get("/persons/person/1")).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("{\"name\":\"Peter Lustig\",\"address\":\"Bauwagen 1\"}");
    }

    @Test
    public void liefert_404() throws Exception {
        given(personService.findMyPerson(1)).willThrow(PersonNotFoundException.class);

        MvcResult mvcResult = mockMvc.perform(get("/persons/person/1")).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(404);
    }

}