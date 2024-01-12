package com.final_project_leesanghun_team2.configuration.batch.itemWriter;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CleanupJdbcTagPostItemWriter implements ItemWriter<List<Long>> {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public CleanupJdbcTagPostItemWriter(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public void write(List<? extends List<Long>> items) throws Exception {
		for (List<Long> ids : items) {
			deleteTagPostData(ids);
		}
	}

	private void deleteTagPostData(List<Long> tagPostIds) {
		String deleteQuery = "DELETE FROM tag_post WHERE tag_post_id IN (:tagPostIds)";

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tagPostIds", tagPostIds);

		int deletedRows = namedParameterJdbcTemplate.update(deleteQuery, parameters);

		// 삭제된 행 수 로그 출력
		if (deletedRows != 0) log.info("Deleted rows from tag_post table: {}", deletedRows);
	}
}
