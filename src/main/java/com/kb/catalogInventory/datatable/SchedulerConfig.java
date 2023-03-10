package com.kb.catalogInventory.datatable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="scheduler_config")
public class SchedulerConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "scheduler_name")
	private String schedulerName;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public Date getLastExecutionTime() {
		return lastExecutionTime;
	}

	public void setLastExecutionTime(Date lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public boolean isProcessing() {
		return isProcessing;
	}

	public void setProcessing(boolean isProcessing) {
		this.isProcessing = isProcessing;
	}

	@Column(name="last_execution_time")
	private Date lastExecutionTime;
	
	@Column(name="start_time")
	private Long startTime;
	
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	@Column(name="is_run_schdeuler")
	private boolean isRun;
	
	@Column(name="is_processing")
	private boolean isProcessing;
	
	@Column(name="record_id")
	private Long recoredId;

	public Long getRecoredId() {
		return recoredId;
	}

	public void setRecoredId(Long recoredId) {
		this.recoredId = recoredId;
	}
	
}
