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

}
