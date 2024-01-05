package com.final_project_leesanghun_team2.configuration.batch;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final LikesRepository likesRepository;

	@Bean
	public ItemReader<Post> postItemReader() {
		// 게시물 조회 로직
		JpaPagingItemReader<Post> reader = new JpaPagingItemReader<>();
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryString("SELECT p FROM Post p");
		reader.setPageSize(100);

		return reader;
	}

	@Bean
	public ItemProcessor<Post, Post> postItemProcessor() {
		// 갱신 로직
		return post -> {
			Long likeCount = likesRepository.countByPost(post);
			return post;
		};
	}

	@Bean
	public ItemWriter<Post> postItemWriter() {

		// 게시물 업데이트 로직
		JpaItemWriter<Post> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);

		return writer;
	}

	@Bean
	public Step updateLikeCountStep() {
		return stepBuilderFactory.get("updateLikeCountStep")
				.<Post, Post>chunk(100)
				.reader(postItemReader())
				.processor(postItemProcessor())
				.writer(postItemWriter())
				.build();
	}

	@Bean
	public Job updateLikeCountJob() {
		return jobBuilderFactory.get("updateLikeCountJob")
				.incrementer(new RunIdIncrementer())
				.flow(updateLikeCountStep())
				.end()
				.build();
	}
}
