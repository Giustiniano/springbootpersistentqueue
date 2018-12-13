package com.springboot.exercise.queuemanager.controller;

import com.springboot.exercise.converters.EnqueueJobConverter;
import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobProperties;
import com.springboot.exercise.model.db.JobStatus;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.model.json.CancelRequest;
import com.springboot.exercise.model.json.EnqueueRequest;
import com.springboot.exercise.repository.JobPropertiesRepository;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.repository.JobStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    @DeleteMapping(path="/cancel")
    @Transactional
    public @ResponseBody ResponseEntity<String> cancelJob(@RequestBody CancelRequest request){
        try {
            request.validate();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing job id");
        }
        Optional<Job> persistedJob = jobsRepository.findById(request.getId());
        if(!persistedJob.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job with id " + request.getId().toString() + " not found");
        }
        JobStatus canceledJobStatus = JobStatusId.CANCELED.toJobStatus();
        persistedJob.get().setIdJobStatus(canceledJobStatus);
        jobsRepository.save(persistedJob.get());
        return ResponseEntity.ok("job " + request.getId().toString() + " successfully canceled");
    }

}
