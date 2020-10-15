package com.fastis.repositories;

import com.fastis.data.MembershipType;
import com.fastis.data.UserRole;
import com.fastis.data.UserRoleId;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleId> {
    MembershipType findByUserId(int id);

    UserRole findByUserIdAndByBoardId();
}
