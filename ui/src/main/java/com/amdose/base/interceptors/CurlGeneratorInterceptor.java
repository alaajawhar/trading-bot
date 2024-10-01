package com.amdose.base.interceptors;

import com.amdose.base.constants.AppConstants;
import com.amdose.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Type;
import java.util.Enumeration;

/**
 * @author Alaa jawhar
 */
@Slf4j
@RestControllerAdvice
@Profile(AppConstants.Profiles.NOT_PRODUCTION)
public class CurlGeneratorInterceptor extends AbstractInterceptor {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return Boolean.TRUE;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            log.debug("Curl: [" + requestToCurl(this.httpServletRequest, body) + "]");
        } catch (Exception e) {
            log.error("Error while generating curl Command: ", e);
        }
        return body;
    }

    private static String requestToCurl(HttpServletRequest request, Object requestBody) {

        StringBuilder result = new StringBuilder();

        result.append("curl --location --request ");

        // output method
        result.append(request.getMethod()).append(" ");

        // output url
        result.append("\"").append(request.getRequestURL().toString()).append("\"");

        // output headers
        for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String headerName = (String) headerNames.nextElement();
            result.append(" -H \"").append(headerName).append(": ").append(request.getHeader(headerName)).append("\"");
        }

        // output parameters
        for (Enumeration<String> parameterNames = request.getParameterNames(); parameterNames.hasMoreElements(); ) {
            String parameterName = (String) parameterNames.nextElement();
            result.append(" -d \"").append(parameterName).append("=").append(request.getParameter(parameterName)).append("\"");
        }

        // output body
        if (RequestMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            String body = JsonUtils.convertToString(requestBody);
            if (body.length() > 0) {
                result.append(" -d \'").append(body).append("\'");
            }
        }

        return result.toString();
    }
}
