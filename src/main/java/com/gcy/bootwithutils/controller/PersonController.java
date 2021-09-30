package com.gcy.bootwithutils.controller;


import com.gcy.bootwithutils.model.dto.Person;
import com.gcy.bootwithutils.service.json.JsonService;
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
    private JsonService jsonService;

    @Autowired
    private KieSession session;


    @GetMapping("/query")
    public List<Person> selectPerson() {


        List<Person> result = personService.getPersonList();
        for (Person p : result) {
            session.setGlobal("jsonService", jsonService);
            session.insert(p);
            session.fireAllRules();
        }
        System.out.println("规则引擎返回数据为: " + session.getGlobal("jsonService").toString());
        // session.dispose();
        return result;
    }

}
