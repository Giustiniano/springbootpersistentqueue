package com.springboot.exercise.jpacommon.repository;


import com.springboot.exercise.jpacommon.model.db.JobStatus;
import org.springframework.data.repository.CrudRepository;

public interface JobStatusRepository extends CrudRepository<JobStatus, Integer> {
}
