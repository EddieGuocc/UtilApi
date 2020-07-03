package com.gcy.bootwithutils.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Person {

    private String name;

    private char gender;

    private Integer age;

    private String relationWithOwner;

    public Person(String name, char gender, Integer age, String relationWithOwner) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.relationWithOwner = relationWithOwner;
    }
}
