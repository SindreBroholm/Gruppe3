package com.fastis.security;

import com.fastis.data.*;

import com.fastis.repositories.UserRoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccessVerifier {

    private UserRoleRepository userRoleRepository;

    public AccessVerifier(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }


    public UserRole setUserRole(User user, Board board, MembershipType accessType){
        UserRole userRole = userRoleRepository.findAllByUserIdAndBoardId(user.getId(), board.getId());
        if (userRole == null){
            userRole = new UserRole(
                    user.getId(), board.getId(),
                    MembershipType.FOLLOWER, 0
                    );
        }
        userRole.setMembershipType(accessType);
        return userRoleRepository.save(userRole);
    }

    //will return null if not at least a follower
    public UserRole getUserRole(User user, Board board){
        return userRoleRepository.findAllByUserIdAndBoardId(user.getId(), board.getId());
    }

    //will return an empty list if not at least a follower at a single board
    public List<UserRole> getAllUserRoles(User user){
        return userRoleRepository.findAllByUserId(user.getId());
    }
}
