package com.amdose.base.payloads.signals;

import com.amdose.base.payloads.PaginationResponse;
import lombok.Data;

import java.util.List;

/**
 * @author Alaa Jawhar
 */
@Data
public class GetSignalListResponse extends PaginationResponse {
    private List<SignalItem> list;
}
