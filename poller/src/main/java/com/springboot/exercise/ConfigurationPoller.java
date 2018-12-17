package com.springboot.exercise;

import com.springboot.exercise.model.db.SchedulerSettings;
import com.springboot.exercise.repository.SchedulerSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class ConfigurationPoller {
    @Autowired
    private SchedulerSettingsRepository schedulerSettingsRepository;
    @Transactional(readOnly = true)
    public int getInterval(){
        Optional<SchedulerSettings> schedulerSettings = schedulerSettingsRepository.findById("default");
        if(schedulerSettings.isPresent()){
            return schedulerSettings.get().getIntervalInSeconds();
        } else {
            return 0;
        }
    }
}
