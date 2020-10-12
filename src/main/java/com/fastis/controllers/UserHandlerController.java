package com.fastis.controllers;

import com.fastis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserHandlerController {

    @Autowired
    private UserRepository userRepository;



    /*
    *
    *
    *       SIGN IN
    *
    * */

    @GetMapping("/signin")
    public String viewSignIn(){

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
