package com.gcy.bootwithutils.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName AppleAuthKeys
 * @Description Apple公钥List 一般返回两组数据
 * @Author Eddie
 * @Date 2020/07/16 09:42
 */

@Getter
@Setter
@ToString
public class AppleAuthKeys {
    private List<AppleAuthKey> keys;
}
