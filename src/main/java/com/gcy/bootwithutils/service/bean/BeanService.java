package com.gcy.bootwithutils.service.bean;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class BeanService extends org.springframework.beans.BeanUtils{

    /*
     * @Author gcy
     * @Description 返回对象中为null的属性
     * @Date 15:58 2020/5/13
     * @Param [source]
     * @return java.lang.String[]
     **/
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /*
     * @Author gcy
     * @Description 从源对象复制相同的属性到目标对象
     * @Date 16:19 2020/5/13
     * @Param [src, target]
     * @return void
     **/
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /*
     * @Author gcy
     * @Description 检查某类是否包括某一属性
     * @Date 16:25 2020/5/13
     * @Param [cls, fieldName]
     * @return boolean
     **/
    public static boolean containProperty(Class<?> cls, String fieldName) {
        return null != FieldUtils.getDeclaredField(cls, fieldName, true);
    }
}
