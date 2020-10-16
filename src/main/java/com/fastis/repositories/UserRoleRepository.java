package com.fastis.repositories;

import com.fastis.data.MembershipType;
import com.fastis.data.UserRole;
import com.fastis.data.UserRoleId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleId> {

    UserRole findAllByUserIdAndBoardId(Integer userId, Integer boardId);

    List<UserRole> findAllByUserId(Integer userId);

    @Query("select b.name from UserRole ur join Board b on b.id = ur.boardId where\n" +
            "ur.userId = ?1 order by b.name")
    List<String> getAllBoardsFromUserId(int id);
}
