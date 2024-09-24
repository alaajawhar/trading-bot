package com.amdose.base.payloads.dashboard;

import com.amdose.base.models.enums.DashboardFilterEnum;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class DashboardRequest {
    private DashboardFilterEnum filter;
}
