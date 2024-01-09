package com.final_project_leesanghun_team2.repository.follow;

import static com.final_project_leesanghun_team2.domain.entity.QFollow.follow;

import com.final_project_leesanghun_team2.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;

public class FollowRepositoryImpl implements FollowRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public FollowRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<User> findTop5UsersWithMostFollowers() {
		return queryFactory
				.select(follow.followUser)
				.from(follow)
				.groupBy(follow.followUser)
				.orderBy(follow.count().desc())
				.limit(5)
				.fetch();
	}

}
