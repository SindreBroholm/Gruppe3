package com.fastis.data;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MembershipType {
    @Id
    @NotNull
    private String name;

    public MembershipType() {
    }
    public MembershipType(String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
