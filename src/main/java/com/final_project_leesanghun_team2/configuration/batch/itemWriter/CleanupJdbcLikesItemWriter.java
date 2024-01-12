package com.final_project_leesanghun_team2.configuration.batch.itemWriter;

import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CleanupJdbcLikesItemWriter implements ItemWriter<List<Long>> {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public CleanupJdbcLikesItemWriter(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public void write(List<? extends List<Long>> items) throws Exception {
		for (List<Long> ids : items) {
			deleteLikesData(ids);
		}
	}

	private void deleteLikesData(List<Long> likesIds) {
		String deleteQuery = "DELETE FROM likes WHERE likes_id IN (:likesIds)";

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("likesIds", likesIds);

		int deletedRows = namedParameterJdbcTemplate.update(deleteQuery, parameters);

		// 삭제된 행 수 로그 출력
		if (deletedRows != 0) log.info("Deleted rows from likes table: {}", deletedRows);
	}

}
