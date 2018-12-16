package com.springboot.exercise.repository;

import com.springboot.exercise.model.db.SchedulerSettings;
import org.springframework.data.repository.CrudRepository;

public interface SchedulerSettingsRepository extends CrudRepository<SchedulerSettings, String> {

}
