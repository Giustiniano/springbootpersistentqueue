package com.springboot.exercise.model.json;

import com.springboot.exercise.utils.ValidationUtils;

public class SetSchedulerTimeoutRequest {

    private Integer intervalInSeconds;

    public Integer getIntervalInSeconds() {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(Integer intervalInSeconds) {
        this.intervalInSeconds = intervalInSeconds;
    }
    public void validate(){
        String fieldName = "scheduler timeout";
        ValidationUtils.throwIllegalArgExIfNull(intervalInSeconds, fieldName);
        ValidationUtils.throwIllegalArgExIfNegative(intervalInSeconds, fieldName);

    }
}
