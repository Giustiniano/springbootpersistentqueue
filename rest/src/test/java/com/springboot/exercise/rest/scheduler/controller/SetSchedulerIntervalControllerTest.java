package com.springboot.exercise.rest.scheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.model.db.SchedulerSettings;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.repository.SchedulerSettingsRepository;
import com.springboot.exercise.rest.model.json.ChangePriorityRequest;
import com.springboot.exercise.rest.model.json.SetSchedulerIntervalRequest;
import com.springboot.exercise.rest.queuemanager.controller.ChangePriorityRequestController;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SetSchedulerIntervalControllerTest {

    private SetSchedulerIntervalRequest setSchedulerInteval = new SetSchedulerIntervalRequest();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SchedulerSettingsRepository schedulerSettingsRepository;
    
    private static final String ENDPOINT = "/scheduler/setinterval";
    @Before
    public void before(){
        // values set just to pass request validation
        setSchedulerInteval = new SetSchedulerIntervalRequest();
        setSchedulerInteval.setIntervalInSeconds(10);
    }

    @Test
    public void shouldSetPriority() throws Exception {
        Mockito.when(schedulerSettingsRepository.findById("default")).thenReturn(Optional.of(createSchedulerSettings()));
        String jsonReq = new ObjectMapper().writeValueAsString(setSchedulerInteval);
        mockMvc.perform(put(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isOk()).andReturn();
    }

    @Test
    public void shouldRejectInvalidVerbDelete() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(setSchedulerInteval);
        mockMvc.perform(delete(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectInvalidVerbPost() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(setSchedulerInteval);
        mockMvc.perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectInvalidVerbGet() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(setSchedulerInteval);
        mockMvc.perform(get(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectInvalidVerbHead() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(setSchedulerInteval);
        mockMvc.perform(head(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectInvalidVerbPatch() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(setSchedulerInteval);
        mockMvc.perform(patch(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }



    private SchedulerSettings createSchedulerSettings(){
        SchedulerSettings schedulerSettings = new SchedulerSettings();
        schedulerSettings.setId("default");
        schedulerSettings.setIntervalInSeconds(1);
        return schedulerSettings;
    }
}
