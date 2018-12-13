package com.springboot.exercise.json;

import com.springboot.exercise.utils.ValidationUtils;

import java.util.List;
import java.util.Map;

public class EnqueueRequest {
    private String name = null;
    private List<Map.Entry<String, String>> payload;
    private Integer priority = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map.Entry<String, String>> getPayload() {
        return payload;
    }

    public void setPayload(List<Map.Entry<String, String>> payload) {
        this.payload = payload;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void validate(){
        ValidationUtils.throwIllegalArgExIfNullEmptyOrBlank(name, "job name");
        ValidationUtils.throwIllegalArgExIfNull(priority, "job priority");

    }


}
