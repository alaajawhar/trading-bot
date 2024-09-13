package com.amdose.base.exceptions;

import lombok.AllArgsConstructor;

/**
 * @author Alaa Jawhar
 */
@AllArgsConstructor
public class InvalidRequestException extends RuntimeException {
    private String message;
}
