package com.springboot.exercise.rest.model.json;

public class EnqueuedJobResponse {
    private Integer newJobId;

    public EnqueuedJobResponse(Integer newJobId) {
        this.newJobId = newJobId;
    }

    public Integer getNewJobId() {
        return newJobId;
    }

    public void setNewJobId(Integer newJobId) {
        this.newJobId = newJobId;
    }
}
