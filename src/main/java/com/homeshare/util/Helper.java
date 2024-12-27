package com.homeshare.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Slf4j
public class Helper {

    private Helper() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()).disable(WRITE_DATES_AS_TIMESTAMPS);

    public static String asJsonString(final Object object) {
        try{
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void log(String message){
        log.info(message);
    }

    public static void logError(String message, Exception e){
        log.error(message, e);
    }

}
