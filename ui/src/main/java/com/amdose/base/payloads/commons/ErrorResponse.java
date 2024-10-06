package com.amdose.base.payloads.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alaa Jawhar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String errorUuid;
    private String message;
}
