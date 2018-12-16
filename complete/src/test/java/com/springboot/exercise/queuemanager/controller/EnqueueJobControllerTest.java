package com.springboot.exercise.queuemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobProperties;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.model.json.EnqueueRequest;
import com.springboot.exercise.repository.JobPropertiesRepository;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.repository.JobStatusRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EnqueueJobControllerTest {

    private EnqueueRequest enqueueRequest = new EnqueueRequest();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JobRepository jobRepository;
    @MockBean
    private JobStatusRepository jobStatusRepository;
    @MockBean
    private JobPropertiesRepository jobPropertiesRepository;


    public void createJobStatusRecords(){
        for(JobStatusId jobStatus : JobStatusId.values()){
            jobStatusRepository.save(jobStatus.toJobStatus());
        }
    }
    @Before
    public void before(){
        createJobStatusRecords();
        enqueueRequest.setName("some job");
        enqueueRequest.setPriority(1);
        List<Map.Entry<String, String>> payload = new ArrayList<>();
        payload.add(new AbstractMap.SimpleEntry<>("property1", "value1"));
        payload.add(new AbstractMap.SimpleEntry<>("property2", "value2"));
        enqueueRequest.setPayload(payload);
    }

    @Test
    public void shouldCreateJob() throws Exception {
        ArgumentCaptor<Job> jobCaptor = ArgumentCaptor.forClass(Job.class);
        ArgumentCaptor<Iterable<JobProperties>> jobPropertiesCaptor = ArgumentCaptor.forClass(Iterable.class);
        int fakeIdJob = 1;
        // configure the jobRepository mock to validate that the object to be saved has the same priority contained in
        // JSON request. Assert that the job to be saved has the correct job status (QUEUED) and contains the date
        Mockito.when(jobRepository.save(jobCaptor.capture())).thenAnswer((Answer<Job>) invocationOnMock -> {
            Job capturedJob = jobCaptor.getValue();
            capturedJob.setIdJob(fakeIdJob); // add a fake id to this job to simulate it being saved on the db
            Assert.assertThat(capturedJob.getIdJobStatus(), CoreMatchers.equalTo(JobStatusId.QUEUED.toJobStatus()));
            Assert.assertThat(capturedJob.getPriority(), CoreMatchers.equalTo(enqueueRequest.getPriority()));
            Assert.assertThat(capturedJob.getName(), CoreMatchers.equalTo(enqueueRequest.getName()));
            Assert.assertThat(capturedJob.getDate(), CoreMatchers.notNullValue(Date.class));
            return capturedJob;
        });
        Mockito.when(jobPropertiesRepository.saveAll(jobPropertiesCaptor.capture())).thenAnswer(new Answer<Iterable<JobProperties>>() {
            @Override
            public Iterable<JobProperties> answer(InvocationOnMock invocationOnMock) throws Throwable {
                // check that all the saved properties for this job have the same jobId
                int propCounter = 0;
                Iterator<JobProperties> jobPropIterator = jobPropertiesCaptor.getValue().iterator();
                while(jobPropIterator.hasNext()){
                    JobProperties jp = jobPropIterator.next();
                    Assert.assertThat(jp.getIdJob(), CoreMatchers.equalTo(fakeIdJob));
                    propCounter++;
                }
                // if the original request contained x elements as payload, the call to saveAll should return the same number of elements
                Assert.assertThat(enqueueRequest.getPayload().size(), CoreMatchers.equalTo(propCounter));
                return null;
            }
        });

        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(post("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isCreated());
    }

    @Test
    public void shouldRejectJobInvalidVerbPut() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(put("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbDelete() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(delete("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbGet() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(get("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbHead() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(head("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobInvalidVerbPatch() throws Exception {
        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(patch("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isMethodNotAllowed());
    }

    @Test
    public void shouldRejectJobNoName() throws Exception {
        enqueueRequest.setName(null);
        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(post("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isBadRequest());
    }
    @Test
    public void shouldRejectJobNoPriority() throws Exception {
        enqueueRequest.setPriority(null);
        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(post("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isBadRequest());
    }
    @Test
    public void shouldRejectJobNoPayload() throws Exception {
        enqueueRequest.setPayload(null);
        String jsonReq = new ObjectMapper().writeValueAsString(enqueueRequest);
        mockMvc.perform(post("/queuemanager/enqueue").contentType(MediaType.APPLICATION_JSON).content(jsonReq)).andExpect(
                status().isBadRequest());
    }
}
