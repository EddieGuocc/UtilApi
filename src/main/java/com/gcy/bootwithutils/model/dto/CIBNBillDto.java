package com.gcy.bootwithutils.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class CIBNBillDto {

    private Integer id;

    private BigDecimal price;

    private String orderId;

    private long createTime;

    private Integer userId;

    private Integer productId;

    private String productName;

    public CIBNBillDto(BigDecimal price, String orderId, long createTime, Integer userId, Integer productId, String productName) {
        this.price = price;
        this.orderId = orderId;
        this.createTime = createTime;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
    }
}
