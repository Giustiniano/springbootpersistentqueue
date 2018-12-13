package com.springboot.exercise.repository;

import com.springboot.exercise.model.db.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Integer> {

}
