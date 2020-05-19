package com.gcy.bootwithutils.common.dto;


/*
 * @Author gcy
 * @Description common pagination response params
 * @Date 11:07 2020/5/15
 * @Param
 * @return
 **/

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResponse extends PaginationRequest{

    /*
     * @Author gcy
     * @Description using JSONField to serialize field total
     * @Date 14:13 2020/5/15
     * @Param
     * @return
     **/
    @JSONField(serializeUsing = LongCodec.class)
    private long total;

    private int pages;


    /*
     * three ways using @JSONField
     * 1. @JSONField(format = "yyyy-MM-dd HH:mm:ss")  convert to given format
     * 2. @JSONField(name="id")       set 'id' field into instance which in json xxx field
     *      public void setXXX() {...}
     * 3. @JSONField(name="test")   serialize xxx field into json 'test' field
　　        public Integer getXXX() {...}
     **/
}
