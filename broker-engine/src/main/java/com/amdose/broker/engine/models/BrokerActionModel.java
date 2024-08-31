package com.amdose.broker.engine.models;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Alaa Jawhar
 */
@Data
public class BrokerActionModel {
    private String requestAsJson;
    private String responseAsJson;
    private boolean success;
    private String error;

    public boolean getSuccess() {
        return StringUtils.isBlank(this.error);
    }
}
