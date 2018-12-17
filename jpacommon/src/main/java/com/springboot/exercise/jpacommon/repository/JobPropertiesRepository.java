package com.springboot.exercise.jpacommon.repository;

import com.springboot.exercise.jpacommon.model.db.JobProperties;
import org.springframework.data.repository.CrudRepository;

public interface JobPropertiesRepository extends CrudRepository<JobProperties, Integer> {
}
