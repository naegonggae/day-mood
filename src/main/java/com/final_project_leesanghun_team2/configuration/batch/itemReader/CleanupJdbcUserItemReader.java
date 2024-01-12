package com.final_project_leesanghun_team2.configuration.batch.itemReader;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CleanupJdbcUserItemReader implements ItemReader<List<Long>> {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public CleanupJdbcUserItemReader(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Long> read() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime fourAM = now.with(LocalTime.of(4, 0)); // 새벽 4시
		LocalDateTime thirtyDaysAgo = fourAM.minusDays(30); // 30일 전

//		List<Long> ids = jdbcTemplate.queryForList(
//				"SELECT user_id FROM users WHERE deleted_at IS NOT NULL AND deleted_at < ?",
//				Long.class, Timestamp.valueOf(thirtyDaysAgo));
		List<Long> ids = jdbcTemplate.queryForList(
				"SELECT user_id FROM user WHERE deleted_at IS NOT NULL AND deleted_at < ?",
				Long.class, Timestamp.valueOf(thirtyDaysAgo));

		return ids.isEmpty() ? null : ids;
	}
}
