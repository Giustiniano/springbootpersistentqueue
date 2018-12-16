package com.springboot.exercise.queuemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.model.json.CancelRequest;
import com.springboot.exercise.repository.JobRepository;
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
public class CancelJobControllerTest {

    private CancelRequest cancelRequest = new CancelRequest();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JobRepository jobRepository;
    
    private static final String ENDPOINT = "/queuemanager/cancel";
    @Before
    public void before(){
        cancelRequest = new CancelRequest();
        cancelRequest.setId(1);
    }

    @Test
    public void shouldCancelJob() throws Exception {
        Job jobToCancel = new Job();
        jobToCancel.setIdJobStatus(JobStatusId.QUEUED.toJobStatus());
        jobToCancel.setIdJob(1);
        jobToCancel.setPriority(1);
        jobToCancel.setName("not a real job");
        Mockito.when(jobRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(jobToCancel));
        String jsonReq = new ObjectMapper().writeValueAsString(cancelRequest);
        mockMvc.perform(delete(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isOk());
    }

    @Test
    public void shouldRejectJobInvalidVerbPut() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(cancelRequest);
        mockMvc.perform(put(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbPost() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(cancelRequest);
        mockMvc.perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbGet() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(cancelRequest);
        mockMvc.perform(get(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbHead() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(cancelRequest);
        mockMvc.perform(head(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbPatch() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(cancelRequest);
        mockMvc.perform(patch(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobNoId() throws Exception {
        cancelRequest.setId(null);
        String jsonReq = new ObjectMapper().writeValueAsString(cancelRequest);
        mockMvc.perform(delete(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isBadRequest());
    }
    @Test
    public void shouldRejectJobNotExists() {
        JobRepository jobRepository = Mockito.mock(JobRepository.class);
        CancelRequestController cancelRequestController = new CancelRequestController();
        Mockito.when(jobRepository.findById(cancelRequest.getId())).thenReturn(Optional.empty());
        cancelRequestController.setJobRepository(jobRepository);
        Assert.assertThat(cancelRequestController.cancelJob(cancelRequest).getStatusCode(), CoreMatchers.equalTo(HttpStatus.NOT_FOUND));
    }
    @Test
    public void shouldDoNothingJobAlreadyCanceled() {
        JobRepository jobRepository = Mockito.mock(JobRepository.class);
        CancelRequestController cancelRequestController = new CancelRequestController();
        Job alreadyCanceledJob = new Job();
        alreadyCanceledJob.setIdJobStatus(JobStatusId.CANCELED.toJobStatus());
        Mockito.when(jobRepository.findById(cancelRequest.getId())).thenReturn(Optional.of(alreadyCanceledJob));
        cancelRequestController.setJobRepository(jobRepository);
        Assert.assertThat(cancelRequestController.cancelJob(cancelRequest).getStatusCode(), CoreMatchers.equalTo(HttpStatus.NOT_MODIFIED));
    }

    @Test
    public void shouldDoNothingJobAlreadyExecuted() {
        JobRepository jobRepository = Mockito.mock(JobRepository.class);
        CancelRequestController cancelRequestController = new CancelRequestController();
        Job alreadyExecutedJob = new Job();
        alreadyExecutedJob.setIdJobStatus(JobStatusId.DONE.toJobStatus());
        Mockito.when(jobRepository.findById(cancelRequest.getId())).thenReturn(Optional.of(alreadyExecutedJob));
        cancelRequestController.setJobRepository(jobRepository);
        Assert.assertThat(cancelRequestController.cancelJob(cancelRequest).getStatusCode(), CoreMatchers.equalTo(HttpStatus.GONE));
    }
    @Test
    public void shouldDoNothingJobIsBeingRun() {
        JobRepository jobRepository = Mockito.mock(JobRepository.class);
        CancelRequestController cancelRequestController = new CancelRequestController();
        Job alreadyExecutedJob = new Job();
        alreadyExecutedJob.setIdJobStatus(JobStatusId.PROCESSING.toJobStatus());
        Mockito.when(jobRepository.findById(cancelRequest.getId())).thenReturn(Optional.of(alreadyExecutedJob));
        cancelRequestController.setJobRepository(jobRepository);
        Assert.assertThat(cancelRequestController.cancelJob(cancelRequest).getStatusCode(), CoreMatchers.equalTo(HttpStatus.GONE));
    }

    private Job createJob(){
        Job job = new Job();
        job.setIdJob(1);
        job.setPriority(1);
        job.setIdJobStatus(JobStatusId.QUEUED.toJobStatus());
        return job;
    }
}
