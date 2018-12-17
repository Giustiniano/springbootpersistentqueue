package com.springboot.exercise.jpacommon.model.db;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
// this is a job entry in the database
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idJob;
	private String name;
	private int priority;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_Job_idJob", referencedColumnName = "idJob")
	private List<JobProperties> payload;
	private Date date;
	@ManyToOne
	@JoinColumn(name = "idJobStatus")
	private JobStatus idJobStatus;

	public int getIdJob() {
		return idJob;
	}

	public void setIdJob(int idJob) {
		this.idJob = idJob;
	}

	public List<JobProperties> getPayload() {
		return payload;
	}

	public void setPayload(List<JobProperties> payload) {
		this.payload = payload;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

    public JobStatus getIdJobStatus() {
        return idJobStatus;
    }

    public void setIdJobStatus(JobStatus idJobStatus) {
        this.idJobStatus = idJobStatus;
    }
}