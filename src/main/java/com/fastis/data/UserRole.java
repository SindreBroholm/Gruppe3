package com.fastis.data;

import com.fastis.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@IdClass(UserRoleId.class)
public class UserRole {

    @Id
    @Column(insertable = false, updatable = false)
    private Integer userId;

    @Id
    @Column(insertable = false, updatable = false)
    private Integer boardId;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private int numbOfMembers;

    private boolean pendingMember = false;

    public UserRole() {
    }

    public UserRole(Integer userId, Integer boardId, MembershipType membershipType, int numbOfMembers) {
        this.userId = userId;
        this.boardId = boardId;
        this.membershipType = membershipType;
        this.numbOfMembers = numbOfMembers;
    }

    public boolean isPendingMember() {
        return pendingMember;
    }

    public void setPendingMember(boolean pendingMember) {
        this.pendingMember = pendingMember;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }


    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public int getNumbOfMembers() {
        return numbOfMembers;
    }

    public void setNumbOfMembers(int numberOfMembers) {
        this.numbOfMembers = numberOfMembers;
    }

}
