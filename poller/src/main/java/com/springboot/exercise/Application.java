package com.springboot.exercise;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobProperties;
import com.springboot.exercise.model.db.JobStatus;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.repository.JobRepository;
import com.springboot.exercise.repository.JobStatusRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.print.Pageable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class, org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration.class, org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration.class})
@SpringBootApplication
@ComponentScan
@Controller
public class Application {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobStatusRepository jobStatusRepository;
    private String query = "select job from Job job where job.idJobStatus = :jobStatus order by priority DESC, date ASC";

    private Date lastExecution;
    private static final Logger logger;
    private static final DateFormat df;
    private static final String pattern = "EEE, d MMM yyyy HH:mm:ss";

    static {
        logger = Logger.getLogger("my.logger");
        logger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
        df = new SimpleDateFormat(pattern);
    }

    public static void main(String[] args) {

        new SpringApplicationBuilder(Application.class).web(WebApplicationType.NONE).run(args);
        System.out.print("hello");
    }
    //@Transactional

    public void run(String... args) throws Exception {
        ConfigurationPoller configurationPoller = new ConfigurationPoller();
        JobStatus done = jobStatusRepository.findById(3).get();
        List<Job> jobs = jobRepository.getNextJobToExecute(JobStatusId.QUEUED.toJobStatus(), PageRequest.of(1,1));
        while (jobs != null && !jobs.isEmpty()) {

            Job nextJobToExecute = null;
            nextJobToExecute = jobs.get(0);
            if (nextJobToExecute != null) {
                Job job = nextJobToExecute;
                String message = String.format("Now (%s) executing job enqueued on %s, with name %s having priority %d with the following payload: %s", df.format(new Date()), df.format(job.getDate()), job.getName(), job.getPriority(), printPayload(job.getPayload()) );
                logger.info(message);
                job.setIdJobStatus(done);
                jobRepository.save(job);
            }
         jobs = jobRepository.getNextJobToExecute(JobStatusId.QUEUED.toJobStatus(), PageRequest.of(1,1));
        }
    }
    private String printPayload(List<JobProperties> payload){
        StringBuilder sb = new StringBuilder("\n");
        for(JobProperties jobProperty : payload){
            sb = sb.append(jobProperty.getPropertyKey()).append(" = ").append(jobProperty.getPropertyValue());
        }
        return sb.toString();
    }



}