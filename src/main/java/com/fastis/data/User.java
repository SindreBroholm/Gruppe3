package com.fastis.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    @NotBlank(message = "")
    private String email;
    @Size(min = 2, max = 150)
    private String firstname;
    @Size(min = 2, max = 150)
    private String lastname;
    @Size(min = 6, max = 300)
    @NotEmpty
    private String password;
    @Size(min = 6, max = 300)
    @NotEmpty
    private String passwordRepeat;

    private String phone_number;

    public User() {
    }

    public User(String email, String firstname, String lastname, String password, String phone_number, String passwordRepeat) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.phone_number = phone_number;
        this.passwordRepeat = passwordRepeat;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String number) {
        this.phone_number = number;
    }

    public boolean isPasswordEqual() {
        if (password.equals(passwordRepeat)) {
            return true;
        } else {
            return false;
        }
    }
}

