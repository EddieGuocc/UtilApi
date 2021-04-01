package com.gcy.bootwithutils.model.vo;

import lombok.*;

/**
 * @ClassName StudentVo
 * @Description TODO
 * @Author Eddie
 * @Date 2021/03/31 14:48
 */
@Getter
@Setter
@NoArgsConstructor
public class StudentVo {

    private Long id;

    private String name;

    private Integer age;

    public StudentVo(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
