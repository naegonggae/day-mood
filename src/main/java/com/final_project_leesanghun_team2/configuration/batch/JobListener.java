package com.final_project_leesanghun_team2.configuration.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobListener extends JobExecutionListenerSupport {

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Batch Job Completed Successfully!");
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			log.error("Batch Job Failed!");
		}
	}
}