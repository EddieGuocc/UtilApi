package com.gcy.bootwithutils.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class BusinessBillDto {

    private Integer id;

    private BigDecimal price;

    private String orderId;

    private Integer createTime;

    private Integer userId;
}
