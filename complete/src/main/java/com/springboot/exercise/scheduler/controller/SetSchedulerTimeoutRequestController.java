package com.springboot.exercise.scheduler.controller;

import com.springboot.exercise.model.db.SchedulerSettings;
import com.springboot.exercise.model.json.SetSchedulerTimeoutRequest;
import com.springboot.exercise.repository.SchedulerSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

// the endpoint accepting enqueue requests

@Controller
@RequestMapping(path="/scheduler")
public class SetSchedulerTimeoutRequestController {
    @Autowired
    private SchedulerSettingsRepository schedulerSettingsRepository;
    @PutMapping(path="/setinterval")
    @Transactional
    public @ResponseBody ResponseEntity<String> setTimeout(@RequestBody SetSchedulerTimeoutRequest request){
        try {
            request.validate();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("interval either not present or negative");
        }
        SchedulerSettings storedSettings = null;
        Optional<SchedulerSettings> optStoredSettings = schedulerSettingsRepository.findById("default");
        if(optStoredSettings.isPresent()){
            storedSettings = optStoredSettings.get();
        }
        if(storedSettings == null) {
            storedSettings = new SchedulerSettings();
        }

        if(storedSettings.getIntervalInSeconds().equals(request.getIntervalInSeconds())){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("interval already set to " + request.getIntervalInSeconds() + " seconds");
        } else {
            storedSettings.setIntervalInSeconds(request.getIntervalInSeconds());
            schedulerSettingsRepository.save(storedSettings);
            return ResponseEntity.ok("scheduler interval now set to " + request.getIntervalInSeconds() + " seconds");
        }
    }

}
