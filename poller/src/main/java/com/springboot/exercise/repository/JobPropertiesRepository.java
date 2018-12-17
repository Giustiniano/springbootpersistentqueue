package com.springboot.exercise.repository;

import com.springboot.exercise.model.db.JobProperties;
import org.springframework.data.repository.CrudRepository;

public interface JobPropertiesRepository extends CrudRepository<JobProperties, Integer> {
}
