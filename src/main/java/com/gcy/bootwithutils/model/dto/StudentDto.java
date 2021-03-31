package com.gcy.bootwithutils.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName StudentDto
 * @Description TODO
 * @Author Eddie
 * @Date 2021/03/31 14:46
 */
@Getter
@Setter
@AllArgsConstructor
public class StudentDto {

    private Long id;

    private String name;

    private Integer age;
}
