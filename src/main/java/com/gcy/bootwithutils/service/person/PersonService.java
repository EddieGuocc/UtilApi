package com.gcy.bootwithutils.service.person;

import com.gcy.bootwithutils.dao.PersonDao;
import com.gcy.bootwithutils.model.dto.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonDao personDao;

    public List<Person> getPersonList() {
        return personDao.getAll();
    }
}
