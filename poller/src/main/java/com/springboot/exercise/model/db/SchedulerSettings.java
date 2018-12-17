package com.springboot.exercise.model.db;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SchedulerSettings {
    @Id
    private String id = "default";
    private Integer intervalInSeconds = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIntervalInSeconds() {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(Integer timeoutInSeconds) {
        this.intervalInSeconds = timeoutInSeconds;
    }
}
