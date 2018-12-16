package com.springboot.exercise.model.db;
import javax.persistence.*;
@Entity
public class JobStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idJobStatus")
    private int idJobStatus;
    private String description;

    public int getIdJobStatus() {
        return idJobStatus;
    }

    public void setIdJobStatus(int idJobStatus) {
        this.idJobStatus = idJobStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object anObject){
        if(!(anObject instanceof JobStatus)){
            return false;
        }
        return this.getIdJobStatus() == ((JobStatus) anObject).idJobStatus;
    }
}
