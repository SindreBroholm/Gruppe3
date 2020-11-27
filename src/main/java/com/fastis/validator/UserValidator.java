package com.fastis.validator;

import com.fastis.data.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "firstname", "firstname.empty", "Firstname missing");
        ValidationUtils.rejectIfEmpty(errors, "lastname", "lastname.empty", "Lastname missing");
        ValidationUtils.rejectIfEmpty(errors, "email", "email.empty", "Email is not valid");
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty", "Password must be over 6 characters long");
        ValidationUtils.rejectIfEmpty(errors, "passwordRepeat", "password.", "Passwords must match");

    }
}
