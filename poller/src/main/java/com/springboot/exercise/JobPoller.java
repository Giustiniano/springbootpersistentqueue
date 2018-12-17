package com.springboot.exercise;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.repository.JobRepository;
import org.springframework.data.domain.PageRequest;

import javax.print.attribute.standard.JobStateReasons;
import java.util.List;

public class JobPoller {

    private JobRepository jobRepository;
    public JobPoller(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }

    public Job getNextJob(){
        List<Job> jobs = jobRepository.getNextJobToExecute(JobStatusId.QUEUED.toJobStatus(), PageRequest.of(1,1));
        if(jobs.isEmpty()){
            return null;
        }
        else return jobs.get(0);
    }
}
