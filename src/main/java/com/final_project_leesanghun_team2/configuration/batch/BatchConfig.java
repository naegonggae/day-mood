package com.final_project_leesanghun_team2.configuration.batch;

import com.final_project_leesanghun_team2.configuration.batch.itemReader.CleanupJdbcCommentItemReader;
import com.final_project_leesanghun_team2.configuration.batch.itemReader.CleanupJdbcFollowItemReader;
import com.final_project_leesanghun_team2.configuration.batch.itemReader.CleanupJdbcLikesItemReader;
import com.final_project_leesanghun_team2.configuration.batch.itemReader.CleanupJdbcPostItemReader;
import com.final_project_leesanghun_team2.configuration.batch.itemReader.CleanupJdbcTagPostPostItemReader;
import com.final_project_leesanghun_team2.configuration.batch.itemReader.CleanupJdbcUserItemReader;
import com.final_project_leesanghun_team2.configuration.batch.itemWriter.CleanupJdbcCommentItemWriter;
import com.final_project_leesanghun_team2.configuration.batch.itemWriter.CleanupJdbcFollowItemWriter;
import com.final_project_leesanghun_team2.configuration.batch.itemWriter.CleanupJdbcLikesItemWriter;
import com.final_project_leesanghun_team2.configuration.batch.itemWriter.CleanupJdbcPostItemWriter;
import com.final_project_leesanghun_team2.configuration.batch.itemWriter.CleanupJdbcTagPostItemWriter;
import com.final_project_leesanghun_team2.configuration.batch.itemWriter.CleanupJdbcUserItemWriter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	// listener
	private final JobListener jobListener;
	private final StepListener stepListener;

	// reader
	private final CleanupJdbcUserItemReader cleanupJdbcUserItemReader;
	private final CleanupJdbcPostItemReader cleanupJdbcPostItemReader;
	private final CleanupJdbcTagPostPostItemReader cleanupJdbcTagPostPostItemReader;
	private final CleanupJdbcCommentItemReader cleanupJdbcCommentItemReader;
	private final CleanupJdbcFollowItemReader cleanupJdbcFollowItemReader;
	private final CleanupJdbcLikesItemReader cleanupJdbcLikesItemReader;

	// writer
	private final CleanupJdbcUserItemWriter cleanupJdbcUserItemWriter;
	private final CleanupJdbcPostItemWriter cleanupJdbcPostItemWriter;
	private final CleanupJdbcTagPostItemWriter cleanupJdbcTagPostItemWriter;
	private final CleanupJdbcCommentItemWriter cleanupJdbcCommentItemWriter;
	private final CleanupJdbcFollowItemWriter cleanupJdbcFollowItemWriter;
	private final CleanupJdbcLikesItemWriter cleanupJdbcLikesItemWriter;

	@Bean
	public Step userCleanupStep() {
		return stepBuilderFactory.get("userCleanupStep")
				.<List<Long>, List<Long>>chunk(100)
				.reader(cleanupJdbcUserItemReader)
				.writer(cleanupJdbcUserItemWriter)
				.listener(stepListener)
				.build();
	}
	@Bean
	public Step postCleanupStep() {
		return stepBuilderFactory.get("postCleanupStep")
				.<List<Long>, List<Long>>chunk(100)
				.reader(cleanupJdbcPostItemReader)
				.writer(cleanupJdbcPostItemWriter)
				.listener(stepListener)
				.build();
	}
	@Bean
	public Step tagPostCleanupStep() {
		return stepBuilderFactory.get("tagPostCleanupStep")
				.<List<Long>, List<Long>>chunk(100)
				.reader(cleanupJdbcTagPostPostItemReader)
				.writer(cleanupJdbcTagPostItemWriter)
				.listener(stepListener)
				.build();
	}
	@Bean
	public Step commentCleanupStep() {
		return stepBuilderFactory.get("commentCleanupStep")
				.<List<Long>, List<Long>>chunk(100)
				.reader(cleanupJdbcCommentItemReader)
				.writer(cleanupJdbcCommentItemWriter)
				.listener(stepListener)
				.build();
	}
	@Bean
	public Step followCleanupStep() {
		return stepBuilderFactory.get("followCleanupStep")
				.<List<Long>, List<Long>>chunk(100)
				.reader(cleanupJdbcFollowItemReader)
				.writer(cleanupJdbcFollowItemWriter)
				.listener(stepListener)
				.build();
	}
	@Bean
	public Step likesCleanupStep() {
		return stepBuilderFactory.get("likesCleanupStep")
				.<List<Long>, List<Long>>chunk(100)
				.reader(cleanupJdbcLikesItemReader)
				.writer(cleanupJdbcLikesItemWriter)
				.listener(stepListener)
				.build();
	}

	@Bean
	public Job cleanupJob() {
		return jobBuilderFactory.get("cleanupJob")
				.incrementer(new RunIdIncrementer())
				.listener(jobListener)
				.start(tagPostCleanupStep())
				.next(commentCleanupStep())
				.next(likesCleanupStep())
				.next(postCleanupStep())
				.next(followCleanupStep())
				.next(userCleanupStep())
				.build();
	}
}
