package com.springboot.exercise.rest.scheduler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.exercise.model.db.SchedulerSettings;
import com.springboot.exercise.rest.model.json.GetSchedulerTimeoutResponse;
import com.springboot.exercise.repository.SchedulerSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

// the endpoint accepting enqueue requests

@Controller
@RequestMapping(path="/scheduler")
public class GetSchedulerTimeoutRequestController {
    @Autowired
    private SchedulerSettingsRepository schedulerSettingsRepository;
    @GetMapping(path="/getinterval")
    @Transactional
    public @ResponseBody ResponseEntity<String> getTimeout() throws JsonProcessingException {

        SchedulerSettings storedSettings = null;
        Optional<SchedulerSettings> optStoredSettings = schedulerSettingsRepository.findById("default");
        GetSchedulerTimeoutResponse response = new GetSchedulerTimeoutResponse();
        if(optStoredSettings.isPresent()){
            storedSettings = optStoredSettings.get();
            response.setIntervalInSeconds(storedSettings.getIntervalInSeconds());
        }
        else {
            response.setIntervalInSeconds(0);
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(response));

    }

}
