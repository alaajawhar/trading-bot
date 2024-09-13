package com.amdose.base.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Alaa jawhar
 */
@Slf4j
@Component
public class LanguageUtils {

    private static final String ENGLISH_LANGUAGE = "en";
    private static final String ARABIC_LANGUAGE = "ar";

    public static String resolveMessage(String messageCode) {
        Locale contextLocal = LocaleContextHolder.getLocale();
        try {
            return messageResolver.getMessage(messageCode, null, contextLocal);
        } catch (Exception ex) {
            log.error("PropertyFile: Unable to find message. messageCode: [" + messageCode + "] locale: [" + contextLocal + "]", ex);
            return "PropertyFile: Unable to find message in messageCode: [" + messageCode + "] locale: [" + contextLocal + "]";
        }
    }

    public static String getString(String englishStr, String arabicStr) {
        return (String) getObject(englishStr, arabicStr);
    }

    public static Object getObject(Object englishObject, Object arabicObject) {
        switch (LocaleContextHolder.getLocale().getLanguage().toLowerCase()) {
            case ENGLISH_LANGUAGE:
                return englishObject;
            case ARABIC_LANGUAGE:
                return arabicObject;
            default:
                log.debug("Language Header not Found, return Default Locale(English)");
                return englishObject;
        }
    }

    @Autowired
    private ResourceBundleMessageSource resourceBundleMessageSource;
    private static ResourceBundleMessageSource messageResolver;

    @PostConstruct
    private void init() {
        messageResolver = resourceBundleMessageSource;
    }

}
