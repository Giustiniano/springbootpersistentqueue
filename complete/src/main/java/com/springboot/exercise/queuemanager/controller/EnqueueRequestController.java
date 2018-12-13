package com.springboot.exercise.queuemanager.controller;

import com.springboot.exercise.converters.EnqueueJobConverter;
import com.springboot.exercise.model.db.JobProperties;
import com.springboot.exercise.model.db.JobStatus;
import com.springboot.exercise.repository.JobPropertiesRepository;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.json.EnqueueRequest;
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

import static com.springboot.exercise.converters.EnqueueJobConverter.getJobProperties;

// the endpoint accepting enqueue requests

@Controller    // This means that this class is a Controller
@RequestMapping(path="/queuemanager")
public class EnqueueRequestController {
    @Autowired
    private JobRepository jobsRepository;
    @Autowired
    private JobStatusRepository jobStatusRepository;
    @Autowired
    private JobPropertiesRepository jobPropertiesRepository;
    @PutMapping(path="/enqueue")
    public @ResponseBody ResponseEntity<Integer> enqueuejob(@RequestBody EnqueueRequest request){
        request.validate();
        Job jobToBePersisted = EnqueueJobConverter.toEntity(request);
        jobToBePersisted.setDate(new Date());
        JobStatus scheduledJobStatus = jobStatusRepository.findById(1).get();
        jobToBePersisted.setIdJobStatus(scheduledJobStatus);
        persistJob(jobToBePersisted, request.getPayload());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Transactional
    private void persistJob(Job job, List<Map.Entry<String, String>> payload){
        Job persistedJob = jobsRepository.save(job);
        List<JobProperties> jobProperties = EnqueueJobConverter.getJobProperties(persistedJob.getIdJob(), payload);
        jobPropertiesRepository.saveAll(jobProperties);
    }
}
