package com.gcy.bootwithutils.dao;

import com.gcy.bootwithutils.model.dto.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonDao {
   List<Person> getAll();
}
