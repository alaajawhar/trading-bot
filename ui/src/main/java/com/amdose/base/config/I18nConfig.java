package com.amdose.base.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @author Alaa Jawhar
 */
@Configuration
public class I18nConfig extends AcceptHeaderLocaleResolver {
    private static final String LANGUAGE_HEADER = "Language";
    private static final String MESSAGE_SOURCE_FILE_NAME = "response_messages";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String selectedLanguage = request.getHeader(LANGUAGE_HEADER);
        if (StringUtils.hasText(selectedLanguage) == Boolean.FALSE) {
            return Locale.getDefault();
        }
        return new Locale(selectedLanguage);
    }

    @Bean
    public LocaleResolver localeResolver() {
        I18nConfig languageLocaleResolver = new I18nConfig();
        languageLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return languageLocaleResolver;
    }

    @Bean
    @Primary
    public ResourceBundleMessageSource getResourceBundleMessageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(MESSAGE_SOURCE_FILE_NAME);
        source.setUseCodeAsDefaultMessage(Boolean.TRUE);
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return source;
    }
}
