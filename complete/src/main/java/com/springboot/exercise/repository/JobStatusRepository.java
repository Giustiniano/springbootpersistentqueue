package com.springboot.exercise.repository;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatus;
import org.springframework.data.repository.CrudRepository;

public interface JobStatusRepository extends CrudRepository<JobStatus, Integer> {
}
