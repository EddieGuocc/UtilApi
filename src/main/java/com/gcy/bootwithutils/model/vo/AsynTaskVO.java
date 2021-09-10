package com.gcy.bootwithutils.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AsynTaskVO {

    @NotEmpty
    private String startDate;

    @NotEmpty
    private String endDate;



}
