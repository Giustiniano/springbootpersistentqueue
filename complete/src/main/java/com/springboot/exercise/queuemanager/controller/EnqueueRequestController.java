package com.springboot.exercise.queuemanager.controller;

import com.springboot.exercise.converters.EnqueueJobConverter;
import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobProperties;
import com.springboot.exercise.model.db.JobStatus;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.model.json.EnqueueRequest;
import com.springboot.exercise.repository.JobPropertiesRepository;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.repository.JobStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

// the endpoint accepting enqueue requests

@Controller
@RequestMapping(path="/queuemanager")
public class EnqueueRequestController {
    @Autowired
    private JobRepository jobsRepository;
    @Autowired
    private JobStatusRepository jobStatusRepository;
    @Autowired
    private JobPropertiesRepository jobPropertiesRepository;
    @PostMapping(path="/enqueue")
    public @ResponseBody ResponseEntity<String> enqueuejob(@RequestBody EnqueueRequest request){
        try {
            request.validate();
        }
        catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("the request is missing one or more mandatory parameters");
        }
        Job jobToBePersisted = EnqueueJobConverter.toEntity(request);
        jobToBePersisted.setDate(new Date());
        JobStatus scheduledJobStatus = JobStatusId.QUEUED.toJobStatus();
        jobToBePersisted.setIdJobStatus(scheduledJobStatus);
        persistJob(jobToBePersisted, request.getPayload());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Transactional
    private void persistJob(Job job, List<Map.Entry<String, String>> payload){
        List<JobProperties> jobProperties = new ArrayList<>(payload.size());
        for(Map.Entry<String, String> payloadEntry : payload){
            jobProperties.add(JobProperties.fromPayloadEntry(payloadEntry, job));
        }
        job.setPayload(jobProperties);
        jobsRepository.save(job);
    }
}
