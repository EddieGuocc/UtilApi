package com.gcy.bootwithutils.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ThirdPartBillDto {

    private Integer id;

    private Integer productId;

    private String orderId;

    private String productName;

    private Long createTime;

    private Integer userId;

    public ThirdPartBillDto(Integer productId, String orderId, String productName, Long createTime, Integer userId) {
        this.productId = productId;
        this.orderId = orderId;
        this.productName = productName;
        this.createTime = createTime;
        this.userId = userId;
    }
}
