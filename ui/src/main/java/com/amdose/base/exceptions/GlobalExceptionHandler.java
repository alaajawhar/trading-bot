package com.amdose.base.exceptions;

import com.amdose.base.constants.AppConstants;
import com.amdose.base.constants.ErrorConstants;
import com.amdose.base.payloads.commons.ErrorResponse;
import com.amdose.base.utils.LanguageUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

/**
 * @author Alaa Jawhar
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleBadRequest(Exception ex) {
        log.error("Global Exception has been caught: ", ex);
        return new ResponseEntity<>(
                new ErrorResponse(MDC.get(AppConstants.TRANSACTION_ID), LanguageUtils.resolveMessage(ErrorConstants.GENERIC_ERROR)),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        log.error("Bad Request has been caught: [{}]", ex.getMessage());
        log.trace("Full exception is: ", ex);
        return new ResponseEntity<>(
                new ErrorResponse(MDC.get(AppConstants.TRANSACTION_ID), LanguageUtils.resolveMessage(ErrorConstants.INVALID_REQUEST)),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("ArgumentNotValid Exception has been caught");
        ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField))
                .forEach((fieldName, fieldErrorList) -> {
                    String codeList = fieldErrorList.stream()
                            .map(FieldError::getCode)
                            .collect(Collectors.joining(", "));
                    String objectName = fieldErrorList.get(0).getObjectName();
                    log.error("fieldName: [" + fieldName + "] in [" + objectName + "] should be [" + codeList + "]");
                });
        return new ResponseEntity<>(
                new ErrorResponse(MDC.get(AppConstants.TRANSACTION_ID), LanguageUtils.resolveMessage(ErrorConstants.INVALID_REQUEST)),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error("HTTP General Exception has been caught: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(MDC.get(AppConstants.TRANSACTION_ID), LanguageUtils.resolveMessage(ErrorConstants.INVALID_REQUEST)),
                HttpStatus.BAD_REQUEST);
    }
}
