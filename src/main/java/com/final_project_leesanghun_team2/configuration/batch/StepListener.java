package com.final_project_leesanghun_team2.configuration.batch;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (stepExecution.getStatus() == BatchStatus.FAILED) {

			handleFailure(stepExecution.getFailureExceptions());
			return new ExitStatus("FAILED WITH RETRY");
		}
		return null;
	}

	private void handleFailure(Collection<Throwable> exceptions) {
		for (Throwable exception : exceptions) {
			log.error("Step Fail - Error occurred: {} : {}", exception.getClass().getSimpleName(), exception.getMessage());
		}
	}
}
