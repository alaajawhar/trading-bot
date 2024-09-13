package com.amdose.base.payloads;

import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class PaginationResponse {
    private Long totalCount;
    private Long offset;
}
