package com.amdose.base.payloads.commons;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class PaginationRequest {
    private Long limit;
    private Long offset;
}
