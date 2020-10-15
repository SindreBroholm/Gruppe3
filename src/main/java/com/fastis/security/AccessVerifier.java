package com.fastis.security;

import com.fastis.data.*;

import com.fastis.repositories.UserRepository;
import com.fastis.repositories.UserRoleRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class AccessVerifier {

    private UserRoleRepository userRoleRepository;


    public void setUserRoleToAdminForNewBoard(User user, Board board){
        UserRole userRole = userRoleRepository.findByUserIdAndByBoardId();
        System.out.println(userRole.getMembershipType());
        /*userRole = new UserRole(user.getId(), board.getId(), MembershipType.ADMIN, 0);
        userRoleRepository.save(userRole);*/
    }

    public MembershipType checkAccessLevel(UserRole userRole){
        MembershipType membershipType = userRole.getMembershipType();
        return membershipType;
    }

    public User setAccessForMember(UserRepository userRepository, User user){
        if(userRoleRepository.findByUserId(user.getId()) == MembershipType.ADMIN){
            userRepository.findById(user.getId());

            return user;
        }
        if(userRoleRepository.findByUserId(user.getId()) == MembershipType.LEADER){

        }
        return null;
    }
}
