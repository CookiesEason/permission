package com.example.permission.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author CookiesEason
 * 2019/01/14 11:03
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }


    public static <T> String obj2String(T t) {
        if (t == null) {
            return null;
        }
        try {
            return t instanceof  String ? (String) t : objectMapper.writeValueAsString(t);
        } catch (Exception e) {
            log.warn("parse object to string exception, error:{}", e);
            return null;
        }
    }


    public static <T> T string2Object(String t, TypeReference typeReference) {
        if (t == null || typeReference == null) {
            return null;
        }
        try {
            return  (T) (typeReference.getType().equals(String.class) ? t : objectMapper.readValue(t, typeReference));
        } catch (Exception e) {
            log.warn("parse string to object exception, String:{}, TypeReference<T>:{}, error:{}",
                    t, typeReference.getType(), e);
            return null;
        }
    }


}
