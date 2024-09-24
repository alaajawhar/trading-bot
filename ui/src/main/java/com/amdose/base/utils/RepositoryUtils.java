package com.amdose.base.utils;

import lombok.experimental.UtilityClass;

/**
 * @author Alaa Jawhar
 */
@UtilityClass
public class RepositoryUtils {

    public static final String getCountQuery(String query) {
        return "SELECT COUNT(*) FROM (" + query + ") AS subquery";
    }

    public static final String getSumQuery(String query, String columnName) {
        return "SELECT SUM(" + columnName + ") FROM (" + query + ") AS subquery";
    }
}
