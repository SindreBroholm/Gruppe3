package com.fastis.repositories;

import com.fastis.data.MembershipType;
import com.fastis.data.UserRole;
import com.fastis.data.UserRoleId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleId> {

    UserRole findAllByUserIdAndBoardId(Integer userId, Integer boardId);

    List<UserRole> findAllByUserId(Integer userId);
}
