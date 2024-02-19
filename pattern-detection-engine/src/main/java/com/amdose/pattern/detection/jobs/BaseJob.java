package com.amdose.pattern.detection.jobs;

import com.amdose.database.enums.TimeFrameEnum;
import org.quartz.Job;

/**
 * @author Alaa Jawhar
 */
public abstract class BaseJob implements Job {
    abstract TimeFrameEnum getInterval();
}
