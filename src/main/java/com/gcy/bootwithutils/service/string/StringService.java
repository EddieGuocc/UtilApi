package com.gcy.bootwithutils.service.string;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StringService {

    //Candidate characters
    private static String CandidateString = "ABCDeFGHijkLMNoPQRstUvWxYZ1234567890";

    /*
     * @Author gcy
     * @Description return specified length of random characters
     * @Date 11:04 2020/5/19
     * @Param [length]
     * @return java.lang.String
     **/
    public static String getRandomString(Integer length){
        return RandomStringUtils.random(length, CandidateString);
    }
}
