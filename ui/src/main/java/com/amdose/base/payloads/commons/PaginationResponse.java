package com.amdose.base.payloads.commons;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class PaginationResponse {
    private Long totalCount;
    private Long offset;
}
