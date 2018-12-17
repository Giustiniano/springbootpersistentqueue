package com.springboot.exercise.jpacommon.model.db;
// this is just a convenience to avoid querying the jobstatus table.
// of course this only works if the table and this enum are in sync
public enum JobStatusId {
    QUEUED(1), PROCESSING(2), DONE(3), CANCELED(4);

    private final int statusId;

    JobStatusId(int statusId){
        this.statusId = statusId;
    }

    public JobStatus toJobStatus(){
        JobStatus jobStatus = new JobStatus();
        jobStatus.setDescription(this.name());
        jobStatus.setIdJobStatus(this.statusId);
        return jobStatus;
    }
}
