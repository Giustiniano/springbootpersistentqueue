package com.springboot.exercise.jpacommon.repository;

import com.springboot.exercise.jpacommon.model.db.SchedulerSettings;
import org.springframework.data.repository.CrudRepository;

public interface SchedulerSettingsRepository extends CrudRepository<SchedulerSettings, String> {

}
