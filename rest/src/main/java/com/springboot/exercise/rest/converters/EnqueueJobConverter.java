package com.springboot.exercise.rest.converters;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobProperties;
import com.springboot.exercise.rest.model.json.EnqueueRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// converts an enqueue job json request into an entity ready to be persisted in the database
public class EnqueueJobConverter {

    public static Job toEntity(EnqueueRequest enqueueRequest){
        Job job = new Job();
        job.setName(enqueueRequest.getName());
        job.setPriority(enqueueRequest.getPriority());
        return job;
    }

    public static List<JobProperties> getJobProperties(Job persistedJob, List<Map.Entry<String, String>> payload){
        List<JobProperties> jobProperties = new ArrayList<>();
        for(Map.Entry<String, String> property : payload){
            JobProperties jobProperty = new JobProperties();
            jobProperty.setPropertyKey(property.getKey());
            jobProperty.setPropertyValue(property.getValue());
            jobProperty.setIdJob(persistedJob);
            jobProperties.add(jobProperty);
        }
        return jobProperties;
    }
}
