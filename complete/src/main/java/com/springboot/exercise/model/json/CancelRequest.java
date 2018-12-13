package com.springboot.exercise.model.json;

import com.springboot.exercise.utils.ValidationUtils;

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
