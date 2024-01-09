package com.final_project_leesanghun_team2.repository.comment;

import static com.final_project_leesanghun_team2.domain.entity.QComment.comment;
import static com.final_project_leesanghun_team2.domain.entity.QUser.user;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public CommentRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Comment> findAllByPostAndParentIsNull(Post post, Pageable pageable) {

		List<Comment> content = queryFactory
				.select(comment)
				.from(comment)
				.join(comment.user, user).fetchJoin()
				.where(comment.post.eq(post).and(comment.parent.isNull()))
				.orderBy(comment.createdAt.desc()) // 이후 pageable 설정으로 변하지 않음
				.offset(pageable.getOffset()) // 몇부터 시작할지
				.limit(pageable.getPageSize()) // 몇개씩 조회할지
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(comment.id.count())
				.from(comment)
				.where(comment.post.eq(post).and(comment.parent.isNull()));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<Comment> findAllByPostAndParentComment(Post post, Comment parentComment, Pageable pageable) {

		List<Comment> content = queryFactory
				.select(comment)
				.from(comment)
				.join(comment.user, user).fetchJoin()
				.where(comment.post.eq(post).and(comment.parent.eq(parentComment)))
				.orderBy(comment.createdAt.desc()) // 이후 pageable 설정으로 변하지 않음
				.offset(pageable.getOffset()) // 몇부터 시작할지
				.limit(pageable.getPageSize()) // 몇개씩 조회할지
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(comment.id.count())
				.from(comment)
				.where(comment.post.eq(post).and(comment.parent.eq(parentComment)));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}
}
