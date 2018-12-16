package com.springboot.exercise.queuemanager.controller;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.model.json.CancelRequest;
import com.springboot.exercise.repository.JobPropertiesRepository;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.repository.JobStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

// the endpoint accepting enqueue requests

@Controller
@RequestMapping(path="/queuemanager")
public class CancelRequestController {
    @Autowired
    private JobRepository jobsRepository;
    @Autowired
    private JobStatusRepository jobStatusRepository;
    @Autowired
    private JobPropertiesRepository jobPropertiesRepository;
    private String messagePattern = "Job with id %d %s";

    @DeleteMapping(path="/cancel")
    @Transactional
    public @ResponseBody ResponseEntity<String> cancelJob(@RequestBody CancelRequest request){
        try {
            request.validate();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing job id");
        }
        Optional<Job> optionalPersistedJob = jobsRepository.findById(request.getId());
        if(!optionalPersistedJob.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(messagePattern, request.getId(), "not found"));
        } else {
            Job persistedJob = optionalPersistedJob.get();
            if (persistedJob.getIdJobStatus().equals(JobStatusId.CANCELED.toJobStatus())) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(String.format(messagePattern, request.getId(), "has already been canceled"));
            } else if(persistedJob.getIdJobStatus().equals(JobStatusId.DONE.toJobStatus())){
                return ResponseEntity.status(HttpStatus.GONE).body(String.format(messagePattern, request.getId(), "has already been executed"));
            } else if(persistedJob.getIdJobStatus().equals(JobStatusId.PROCESSING.toJobStatus())){
                return ResponseEntity.status(HttpStatus.GONE).body(String.format(messagePattern, request.getId(), "has already started and cannot be canceled"));
            } else{
                persistedJob.setIdJobStatus(JobStatusId.CANCELED.toJobStatus());
                jobsRepository.save(persistedJob);
                return ResponseEntity.ok(String.format(messagePattern, request.getId(), "successfully canceled"));
            }
        }
    }
    public void setJobRepository(JobRepository jobRepository){
        this.jobsRepository = jobRepository;
    }
}
