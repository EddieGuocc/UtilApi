package com.gcy.bootwithutils.common.dto;

/**
 * @ClassName ResolvedAppleToken
 * @Description APP传入的 identityToken解析后的数据
 * @Author Eddie
 * @Date 2020/07/16 09:50
 */
public class ResolvedAppleToken {

    //APP ID
    private String aud;
    //用户唯一标识
    private String sub;

    private String c_hash;
    //email是否验证
    private String email_verified;
    //token有效时间
    private Integer auth_time;
    //appleid.apple.com
    private String iss;
    //token过期时间
    private Integer exp;

    private Integer iat;
    //email地址
    private String email;

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getC_hash() {
        return c_hash;
    }

    public void setC_hash(String c_hash) {
        this.c_hash = c_hash;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public Integer getAuth_time() {
        return auth_time;
    }

    public void setAuth_time(Integer auth_time) {
        this.auth_time = auth_time;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getIat() {
        return iat;
    }

    public void setIat(Integer iat) {
        this.iat = iat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ResolvedAppleToken{" +
                "aud='" + aud + '\'' +
                ", sub='" + sub + '\'' +
                ", c_hash='" + c_hash + '\'' +
                ", email_verified='" + email_verified + '\'' +
                ", auth_time=" + auth_time +
                ", iss='" + iss + '\'' +
                ", exp=" + exp +
                ", iat=" + iat +
                ", email='" + email + '\'' +
                '}';
    }
}
