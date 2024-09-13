package com.amdose.base.services.base;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Alaa Jawhar
 */
@Slf4j
public abstract class BaseService<REQ, RES> {

    public abstract void validate(REQ request);

    public abstract RES execute(REQ request);

    public RES serve(REQ request) {
        log.info("Validating request...");
        this.validate(request);
        log.info("Request is Valid, Proceeding...");
        RES response = this.execute(request);
        log.info("Returned Success Response");
        return response;
    }

}
