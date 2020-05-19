package com.gcy.bootwithutils.common.dto;


import com.gcy.bootwithutils.service.bean.BeanService;
import com.github.pagehelper.PageInfo;
import lombok.Getter;

import java.util.List;

/*
 * @Author gcy
 * @Description common data structure for pagination
 * @Date 10:55 2020/5/15
 * @Param
 * @return
 **/

@Getter
public class Pagination<E> {

    private List<E> list;

    private PaginationResponse pagination;

    public Pagination(PageInfo<E> pageInfo) {

        this.list = pageInfo.getList();

        PaginationResponse pagination = new PaginationResponse();
        BeanService.copyPropertiesIgnoreNull(pageInfo, pagination);

        this.pagination = pagination;
    }
}
