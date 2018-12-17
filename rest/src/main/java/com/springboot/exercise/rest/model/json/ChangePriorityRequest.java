package com.springboot.exercise.rest.model.json;

import com.springboot.exercise.rest.utils.ValidationUtils;
import com.springboot.exercise.rest.utils.ValidationUtils;

public class ChangePriorityRequest {

    private Integer id;
    private Integer newPriority;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNewPriority() {
        return newPriority;
    }

    public void setNewPriority(Integer newPriority) {
        this.newPriority = newPriority;
    }

    public void validate(){
        ValidationUtils.throwIllegalArgExIfNull(id, "job id");
        ValidationUtils.throwIllegalArgExIfNull(newPriority, "new job priority");
    }

}
