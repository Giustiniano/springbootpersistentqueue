package com.springboot.exercise.model.json;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobProperties;

import java.text.DateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobInfo {
    private Integer jobId;
    private String jobName;
    private String date;
    private Integer priority;
    private List<Map.Entry<String,String>> payload;
    private static DateFormat df = DateFormat.getDateTimeInstance();


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<Map.Entry<String, String>> getPayload() {
        return payload;
    }

    public void setPayload(List<Map.Entry<String, String>> payload) {
        this.payload = payload;
    }

    public Integer getJobId() {

        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public static JobInfo fromJob(Job job){
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobId(job.getIdJob());
        jobInfo.setJobName(job.getName());
        jobInfo.setDate(df.format(job.getDate()));
        jobInfo.setPriority(job.getPriority());
        if(job.getPayload() != null && !job.getPayload().isEmpty()){
            List<Map.Entry<String, String>> payload = new ArrayList<>(job.getPayload().size());
            for(JobProperties jp : job.getPayload()) {
                payload.add(new AbstractMap.SimpleEntry<String, String>(jp.getPropertyKey(), jp.getPropertyValue()));
            }
            jobInfo.setPayload(payload);
        }
        return jobInfo;
    }
}
