package com.gcy.bootwithutils.common.dto;

/**
 * @ClassName ResolvedAppleTokenAlgInfo
 * @Description TODO
 * @Author Eddie
 * @Date 2020/07/21 12:15
 */
public class ResolvedAppleTokenAlgInfo {

    private String kid;

    private String alg;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }
}
