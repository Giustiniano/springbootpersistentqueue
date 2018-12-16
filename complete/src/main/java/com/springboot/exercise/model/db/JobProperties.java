package com.springboot.exercise.model.db;

import javax.persistence.*;
import java.util.Map;

@Entity
public class JobProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idJobProperties;
    private String propertyKey;
    private String propertyValue;
    @ManyToOne
    @JoinColumn(name = "fk_Job_idJob")
    private Job idJob;

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

    public Job getIdJob() {
        return idJob;
    }

    public void setIdJob(Job idJob) {
        this.idJob = idJob;
    }

    public static JobProperties fromPayloadEntry(Map.Entry<String, String> entry, Job job){
        JobProperties jobProperty = new JobProperties();
        jobProperty.setPropertyKey(entry.getKey());
        jobProperty.setPropertyValue(entry.getValue());
        jobProperty.setIdJob(job);
        return jobProperty;
    }
}
