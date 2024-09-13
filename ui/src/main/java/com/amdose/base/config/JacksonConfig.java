package com.amdose.base.config;

import com.amdose.base.constants.AppConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

/**
 * @author Alaa Jawhar
 */
@Configuration
public class JacksonConfig {

    @Bean
    public MappingJackson2HttpMessageConverter objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        builder.failOnEmptyBeans(Boolean.FALSE);
        builder.failOnUnknownProperties(Boolean.FALSE);
        builder.indentOutput(true).dateFormat(new SimpleDateFormat(AppConstants.APPLICATION_DATE_FORMAT));
        return new MappingJackson2HttpMessageConverter(builder.build());
    }
}
