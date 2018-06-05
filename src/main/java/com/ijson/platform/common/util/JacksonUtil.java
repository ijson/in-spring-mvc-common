package com.ijson.platform.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Aaron on 26/12/2016.
 */
public class JacksonUtil {

    public static String toJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public static <E> E fromJson(String json, Class<E> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <E> E fromJson(String json, TypeReference<E> valueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
