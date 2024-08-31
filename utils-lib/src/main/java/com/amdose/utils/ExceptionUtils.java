package com.amdose.utils;

import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class ExceptionUtils {

    public static String getStackTraceAsString(final Throwable throwable) {
        if (throwable == null) {
            return "";
        }

        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
