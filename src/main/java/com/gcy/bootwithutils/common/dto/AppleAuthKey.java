package com.gcy.bootwithutils.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName AppleAuthKeys
 * @Description Apple公钥数据格式
 * @Author Eddie
 * @Date 2020/07/16 09:40
 */

@Getter
@Setter
@ToString
public class AppleAuthKey {
    //RSA签名算法
    private String kty;
    //密钥id标识
    private String kid;

    private String use;
    //RS256加密算法
    private String alg;
    //模数 BASE64编码
    private String n;
    //公钥指数 BASE64编码
    private String e;
}
