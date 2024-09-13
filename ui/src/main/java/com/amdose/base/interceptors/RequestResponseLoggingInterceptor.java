package com.amdose.base.interceptors;

import com.amdose.base.constants.AppConstants;
import com.amdose.base.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Type;

/**
 * @author Alaa jawhar
 */
@Slf4j
@RestControllerAdvice
@Profile(AppConstants.Profiles.NOT_PRODUCTION)
public class RequestResponseLoggingInterceptor extends AbstractInterceptor {

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            log.debug("URI: [" + httpServletRequest.getRequestURI() + "]");
            log.debug("Request: " + JsonUtils.convertToString(body));
        } catch (Exception e) {
            log.error("Error while saving the Body's Request: ", e);
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        try {
            log.debug("Response: " + JsonUtils.convertToString((body)));
        } catch (Exception e) {
            log.error("Error while saving the Body's Response: ", e);
        }
        return super.beforeBodyWrite(body, returnType, selectedContentType, selectedConverterType, request, response);
    }

}
