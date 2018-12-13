package com.springboot.exercise.model.db;

import javax.persistence.*;

@Entity
public class JobProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idJobProperties;
    private String propertyKey;
    private String propertyValue;
    @Column(name = "fk_Job_idJob")
    private int idJob;

    public int getIdJobProperties() {
        return idJobProperties;
    }

    public void setIdJobProperties(int idJobProperties) {
        this.idJobProperties = idJobProperties;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public int getIdJob() {
        return idJob;
    }

    public void setIdJob(int idJob) {
        this.idJob = idJob;
    }
}
