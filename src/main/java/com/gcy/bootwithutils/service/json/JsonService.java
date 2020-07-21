package com.gcy.bootwithutils.service.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JsonService {

    private static final Gson GSON = new Gson();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /*
     * @Author gcy
     * @Description convert Object to json (1)
     * @Date 12:06 2020/5/14
     * @Param [object]
     * @return java.lang.String
     **/
    public static String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return GSON.toJson(object);
    }

    /*
     * @Author gcy
     * @Description convert Object to json (2)
     * @Date 12:09 2020/5/14
     * @Param [object]
     * @return java.lang.String
     **/
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            // ignore
        }
        return null;
    }

    /*
     * @Author gcy
     * @Description convert json to given class
     * @Date 14:06 2020/5/14
     * @Param [jsonData, beanType]
     * @return T
     **/
    public static <T> T jsonToObject(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @Author gcy
     * @Description convert json list to class list for given class
     * @Date 14:13 2020/5/14
     * @Param [jsonData, beanType]
     * @return java.util.List<T>
     **/
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
