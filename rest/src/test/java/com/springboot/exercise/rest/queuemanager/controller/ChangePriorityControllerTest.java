package com.springboot.exercise.rest.queuemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.rest.model.json.ChangePriorityRequest;
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
public class ChangePriorityControllerTest {

    private ChangePriorityRequest changePriorityRequest = new ChangePriorityRequest();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JobRepository jobRepository;
    
    private static final String ENDPOINT = "/queuemanager/changepriority";
    @Before
    public void before(){
        // values set just to pass request validation
        changePriorityRequest = new ChangePriorityRequest();
        changePriorityRequest.setId(1);
        changePriorityRequest.setNewPriority(1);
    }

    @Test
    public void shouldChangePriority() throws Exception {
        Job jobFound = createJob();
        // set a different priority so that the controller will actually do a priority update
        jobFound.setPriority(10);
        Mockito.when(jobRepository.findById(1)).thenReturn(Optional.of(jobFound));
        String jsonReq = new ObjectMapper().writeValueAsString(changePriorityRequest);
        mockMvc.perform(put(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isOk()).andReturn();
    }

    @Test
    public void shouldRejectInvalidVerbDelete() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(changePriorityRequest);
        mockMvc.perform(delete(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbPost() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(changePriorityRequest);
        mockMvc.perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbGet() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(changePriorityRequest);
        mockMvc.perform(get(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbHead() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(changePriorityRequest);
        mockMvc.perform(head(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbPatch() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(changePriorityRequest);
        mockMvc.perform(patch(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobNoId() throws Exception {
        changePriorityRequest.setId(null);
        String jsonReq = new ObjectMapper().writeValueAsString(changePriorityRequest);
        mockMvc.perform(put(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isBadRequest());
    }
    @Test
    public void shouldRejectJobNotExists() {
        JobRepository jobRepository = Mockito.mock(JobRepository.class);
        ChangePriorityRequestController changePriorityRequestController = new ChangePriorityRequestController();
        Mockito.when(jobRepository.findById(changePriorityRequest.getId())).thenReturn(Optional.empty());
        changePriorityRequestController.setJobsRepository(jobRepository);
        Assert.assertThat(changePriorityRequestController.changePriority(changePriorityRequest).getStatusCode(), CoreMatchers.equalTo(HttpStatus.NOT_FOUND));
    }
    @Test
    public void shouldDoNothingJobAlreadyCanceled() {
        JobRepository jobRepository = Mockito.mock(JobRepository.class);
        ChangePriorityRequestController changePriorityRequestController = new ChangePriorityRequestController();
        Job alreadyCanceledJob = new Job();
        alreadyCanceledJob.setIdJobStatus(JobStatusId.CANCELED.toJobStatus());
        Mockito.when(jobRepository.findById(changePriorityRequest.getId())).thenReturn(Optional.of(alreadyCanceledJob));
        changePriorityRequestController.setJobsRepository(jobRepository);
        Assert.assertThat(changePriorityRequestController.changePriority(changePriorityRequest).getStatusCode(), CoreMatchers.equalTo(HttpStatus.GONE));
    }
    @Test
    public void shouldDoNothingJobAlreadyExecuted() {
        JobRepository jobRepository = Mockito.mock(JobRepository.class);
        ChangePriorityRequestController changePriorityRequestController = new ChangePriorityRequestController();
        Job alreadyExecutedJob = new Job();
        alreadyExecutedJob.setIdJobStatus(JobStatusId.DONE.toJobStatus());
        Mockito.when(jobRepository.findById(changePriorityRequest.getId())).thenReturn(Optional.of(alreadyExecutedJob));
        changePriorityRequestController.setJobsRepository(jobRepository);
        Assert.assertThat(changePriorityRequestController.changePriority(changePriorityRequest).getStatusCode(), CoreMatchers.equalTo(HttpStatus.GONE));
    }

    @Test
    public void shouldReportJobAlreadyExecuting() {
        JobRepository jobRepository = Mockito.mock(JobRepository.class);
        ChangePriorityRequestController changePriorityRequestController = new ChangePriorityRequestController();
        Job alreadyExecutingJob = new Job();
        alreadyExecutingJob.setIdJobStatus(JobStatusId.PROCESSING.toJobStatus());
        Mockito.when(jobRepository.findById(changePriorityRequest.getId())).thenReturn(Optional.of(alreadyExecutingJob));
        changePriorityRequestController.setJobsRepository(jobRepository);
        Assert.assertThat(changePriorityRequestController.changePriority(changePriorityRequest).getStatusCode(), CoreMatchers.equalTo(HttpStatus.GONE));
    }

    private Job createJob(){
        Job job = new Job();
        job.setIdJob(1);
        job.setPriority(1);
        job.setIdJobStatus(JobStatusId.QUEUED.toJobStatus());
        return job;
    }
}
