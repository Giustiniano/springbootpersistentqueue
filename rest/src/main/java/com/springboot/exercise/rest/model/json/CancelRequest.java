package com.springboot.exercise.rest.model.json;

import com.springboot.exercise.rest.utils.ValidationUtils;


public class CancelRequest {

    private Integer id;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void validate(){
        ValidationUtils.throwIllegalArgExIfNull(id, "job id");
    }
}
