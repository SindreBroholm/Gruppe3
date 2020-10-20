package com.fastis.data;

import com.fastis.data.User;
import com.fastis.data.UserRole;

public class UserWithRole {

    private User user;
    private UserRole userRole;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
