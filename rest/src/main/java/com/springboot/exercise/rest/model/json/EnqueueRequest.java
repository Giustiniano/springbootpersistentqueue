package com.springboot.exercise.rest.model.json;

import com.springboot.exercise.rest.utils.ValidationUtils;
import com.springboot.exercise.rest.utils.ValidationUtils;

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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void validate(){
        ValidationUtils.throwIllegalArgExIfNullEmptyOrBlank(name, "job name");
        ValidationUtils.throwIllegalArgExIfNull(priority, "job priority");
        ValidationUtils.throwIllegalArgExIfNullOrEmpty(payload, "job payload");

    }


}
