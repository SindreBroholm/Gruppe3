package com.fastis.data;

public enum  MembershipType {

    FOLLOWER("follower"),
    MEMBER("member"),
    LEADER("leader"),
    ADMIN("admin");

    public final String name;

    MembershipType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
