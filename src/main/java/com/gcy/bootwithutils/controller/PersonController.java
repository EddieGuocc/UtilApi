package com.gcy.bootwithutils.controller;


import com.gcy.bootwithutils.model.dto.Person;
import com.gcy.bootwithutils.service.person.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private KieSession session;


    @GetMapping("/query")
    public void selectPerson() {
        Person p = new Person();
        p.setAge(15);
        p.setGender('M');
        p.setName("haha");
        p.setPersonId(123);
        session.insert(p);
        session.fireAllRules();
        // session.dispose();
        // List<Person> result = personService.getPersonList();
        // return result;
    }

}
