package com.springboot.exercise.rest.queuemanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.rest.model.json.JobInfo;
import com.springboot.exercise.repository.JobPropertiesRepository;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.repository.JobStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

// the endpoint accepting enqueue requests

@Controller
@RequestMapping(path="/queuemanager")
public class ListRequestController {
    @Autowired
    private JobRepository jobsRepository;
    @Autowired
    private JobStatusRepository jobStatusRepository;
    @Autowired
    private JobPropertiesRepository jobPropertiesRepository;

    @PersistenceContext
    private EntityManager em;

    @GetMapping(path="/listqueue")
    @Transactional
    public @ResponseBody ResponseEntity<String> listQueue(@RequestParam(value = "priority", required = false) Integer priority) throws JsonProcessingException {
        List<Job> jobs = null;
        if(priority == null) {
            jobs = jobsRepository.getJobsToExecute(JobStatusId.QUEUED.toJobStatus());
        } else {
            jobs = jobsRepository.getJobsToExecuteFilteredByPriority(JobStatusId.QUEUED.toJobStatus(), priority);
        }
        if(jobs == null || jobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("the queue is empty");
        }
        List<JobInfo> jobInfo = new ArrayList<JobInfo>(jobs.size());
        for(Job job : jobs){
            jobInfo.add(JobInfo.fromJob(job));
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(jobInfo));
    }


}
