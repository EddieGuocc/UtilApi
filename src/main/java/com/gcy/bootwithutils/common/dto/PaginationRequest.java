package com.gcy.bootwithutils.common.dto;


import lombok.Getter;
import lombok.Setter;

/*
 * @Author gcy
 * @Description common pagination request params
 * @Date 11:02 2020/5/15
 * @Param
 * @return
 **/

@Getter
@Setter
public class PaginationRequest {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

}
