package com.amdose.pattern.detection.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@UtilityClass
public class JsonUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // TODO: unify it
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(dateFormat);
    }

    public static String convertToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("unable to parse it as json", e);
            return "{\"Error\": \"" + "while JsonUtils.convertToString" + "\"}";
        }
    }

    @SneakyThrows
    public static <T> T convertToObject(String data, Class<?> target) {
        return (T) objectMapper.readValue(data, target);
    }
}
