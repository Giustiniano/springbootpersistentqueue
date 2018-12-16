package com.springboot.exercise.model.json;

public class GetSchedulerTimeoutResponse {
    private Integer intervalInSeconds;

    public Integer getIntervalInSeconds() {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(Integer intervalInSeconds) {
        this.intervalInSeconds = intervalInSeconds;
    }
}
