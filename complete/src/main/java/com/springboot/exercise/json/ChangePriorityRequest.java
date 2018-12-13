package com.springboot.exercise.json;

public class ChangePriorityRequest {

    private long id;
    private int newPriority;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNewPriority() {
        return newPriority;
    }

    public void setNewPriority(int newPriority) {
        this.newPriority = newPriority;
    }
}
