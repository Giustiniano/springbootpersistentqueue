package com.springboot.exercise.rest.scheduler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.exercise.model.db.SchedulerSettings;
import com.springboot.exercise.repository.SchedulerSettingsRepository;
import com.springboot.exercise.rest.model.json.SetSchedulerIntervalRequest;
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
    public @ResponseBody ResponseEntity<String> setInterval(@RequestBody SetSchedulerIntervalRequest request) throws JsonProcessingException {
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
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } else {
            storedSettings.setIntervalInSeconds(request.getIntervalInSeconds());
            return ResponseEntity.ok().build();
        }
    }

}
