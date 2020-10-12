package com.fastis.controllers;

import com.fastis.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserHandlerController {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserHandlerController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    *
    *
    *       SIGN IN
    *
    * */

    @GetMapping("/signin")
    public String viewSignIn(Model model){
        User user = repository.findByEmail("Sindreset@gmail.com");
        model.addAttribute("user", user.getFirstname());
        return "signin";
    }

    @PostMapping("/signin")
    public String signIn(){

        return "/home";
    }
/*
*
*       SIGNUP
*
* */
    @GetMapping("/signup")
    public String viewSignup(){

        return "signup";
    }

    @PostMapping("/signup")
    public String creatUser() {

     return "/home";
    }

/*
*
*
*           PROFILE
*
* */
    @GetMapping("/profile")
    public String viewProfile(){

        return "profile";
    }

}
