package com.amdose.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@UtilityClass
public class JsonUtils<T> {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String convertToString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("unable to parse it as json", e);
            return "{\"Error\": \"" + "while JsonUtils.convertToString" + "\"}";
        }
    }

    @SneakyThrows
    public static <T> T parseJsonToObject(String data, Class<?> target) {
        return (T) OBJECT_MAPPER.readValue(data, target);
    }
}
