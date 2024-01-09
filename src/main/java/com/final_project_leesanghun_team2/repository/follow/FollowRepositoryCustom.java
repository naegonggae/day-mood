package com.final_project_leesanghun_team2.repository.follow;

import com.final_project_leesanghun_team2.domain.entity.User;
import java.util.List;

public interface FollowRepositoryCustom {

	List<User> findTop5UsersWithMostFollowers();


}
