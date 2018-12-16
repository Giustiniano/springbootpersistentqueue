package com.springboot.exercise.repository;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobRepository extends CrudRepository<Job, Integer> {

    @Query(value = "select job from Job job where job.idJobStatus = ?1 order by priority DESC, date ASC")
    List<Job> getJobsToExecute(JobStatus jobStatus);
    @Query(value = "select job from Job job where job.idJobStatus = ?1 and job.priority = ?2 order by date ASC")
    List<Job> getJobsToExecuteFilteredByPriority(JobStatus jobStatus, Integer priority);
}
