package com.gcy.bootwithutils.service.number;


import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class NumberService extends org.apache.commons.lang3.math.NumberUtils{

    /*
     * @Author gcy
     * @Description convert BigDecimal to double
     * @Date 15:06 2020/5/14
     * @Param [decimal]
     * @return java.lang.Double
     * @Tips 关于BigDecimal，不要使用double转BigDecimal，精度会出问题，（原因：二进制无法精确表示十进制小数），
     * 故一定使用字符串来表示小数然后再使用BigDecimal进行操作
     *
     **/
    public static Double getDouble(BigDecimal decimal) {

        if (null == decimal)
            return null;

        return decimal.doubleValue();
    }

    /*
     * @Author gcy
     * @Description generate random number, range in 1 to 999999, length is 6,
     * if length is shorter than 6, add with 0
     * @Date 16:01 2020/5/14
     * @Param []
     * @return java.lang.String
     **/
    public static String randomIntPaddingZero() {
        return Strings.padStart(String.valueOf(RandomUtils.nextInt(1, 999999)), 6, '0');
    }

    /*
     * @Author gcy
     * @Description rounded to two decimals for given BigDecimal
     * @Date 17:40 2020/5/14
     * @Param [value]
     * @return java.math.BigDecimal
     **/
    public static BigDecimal toBigDecimalHalfUp(BigDecimal value) {
        return toScaledBigDecimal(value, INTEGER_TWO, RoundingMode.HALF_UP);
    }
}
