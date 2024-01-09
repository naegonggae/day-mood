package com.final_project_leesanghun_team2.repository.post;


import static com.final_project_leesanghun_team2.domain.entity.QPost.post;
import static com.final_project_leesanghun_team2.domain.entity.QUser.user;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.QPost;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PostRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Post> findAllPost(Pageable pageable) {

		List<Post> content = queryFactory
				.select(post)
				.from(post)
				.join(post.user, user).fetchJoin()
				.orderBy(post.createdAt.desc()) // 이후 pageable 설정으로 변하지 않음
				.offset(pageable.getOffset()) // 몇부터 시작할지
				.limit(pageable.getPageSize()) // 몇개씩 조회할지
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(post.id.count())
				.from(post);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<Post> findAllDefaultPost(Pageable pageable) {

		List<Post> content = queryFactory
				.select(post)
				.from(post)
				.join(post.user, user).fetchJoin()
				.orderBy(post.createdAt.desc()) // 이후 pageable 설정으로 변하지 않음
				.offset(pageable.getOffset()) // 몇부터 시작할지
				.limit(pageable.getPageSize()) // 몇개씩 조회할지
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(post.id.count())
				.from(post);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public List<User> findTop5UsersWithMostPosts() {
		return queryFactory
				.select(post.user)
				.from(post)
				.groupBy(post.user)
				.orderBy(post.count().desc())
				.limit(5)
				.fetch();
	}

}