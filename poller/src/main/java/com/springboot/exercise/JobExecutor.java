package com.springboot.exercise;

import com.springboot.exercise.model.db.Job;
import com.springboot.exercise.model.db.JobProperties;
import com.springboot.exercise.model.db.JobStatusId;
import com.springboot.exercise.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
@Component
public class JobExecutor {

    private JobRepository jobRepository;

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

    public JobExecutor(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Transactional
    public void executeJob(Job job){

    }


}
