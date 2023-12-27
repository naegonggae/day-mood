package com.final_project_leesanghun_team2.configuration.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class JobService {

	private final JobLauncher jobLauncher;
	private final BatchConfig batchConfig;


	@Scheduled(cron = "0 0 4 * * *") // 매일 새벽 4시에 실행
	public void executeBatchJob() {
		try {
			log.info("executeBatchJob 정상 실행");
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(batchConfig.updateLikeCountJob(), jobParameters);
		} catch (JobExecutionException e) {
			log.error("executeBatchJob 실행 중 오류 발생: {}", e.getMessage(), e);
		}
	}

}