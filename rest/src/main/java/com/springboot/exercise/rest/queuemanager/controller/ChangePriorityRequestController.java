package com.springboot.exercise.rest.queuemanager.controller;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.rest.model.json.ChangePriorityRequest;
import com.springboot.exercise.repository.JobPropertiesRepository;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.repository.JobStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

// the endpoint accepting enqueue requests

@Controller
@RequestMapping(path="/queuemanager")
public class ChangePriorityRequestController {
    @Autowired
    private JobRepository jobsRepository;
    @Autowired
    private JobStatusRepository jobStatusRepository;
    @Autowired
    private JobPropertiesRepository jobPropertiesRepository;
    @PutMapping(path="/changepriority")
    @Transactional
    public @ResponseBody ResponseEntity<String> changePriority(@RequestBody ChangePriorityRequest request){
        try {
            request.validate();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing job id");
        }
        Optional<Job> persistedJob = jobsRepository.findById(request.getId());
        if(!persistedJob.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job with id " + request.getId().toString() + " not found");
        }
        if(!persistedJob.get().getIdJobStatus().equals(JobStatusId.QUEUED.toJobStatus())){
            return ResponseEntity.status(HttpStatus.GONE).body("Job with id " + request.getId().toString() + "has already been executed/canceled");
        }
        if(persistedJob.get().getPriority() == request.getNewPriority()){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Job with id " + request.getId().toString() + " already has priority set to " + request.getNewPriority());
        }
        persistedJob.get().setPriority(request.getNewPriority());
        jobsRepository.save(persistedJob.get());
        return ResponseEntity.ok("job " + request.getId().toString() + " has now priority " + request.getNewPriority());
    }
    // used by unit tests
    public void setJobsRepository(JobRepository jobsRepository) {
        this.jobsRepository = jobsRepository;
    }
}
