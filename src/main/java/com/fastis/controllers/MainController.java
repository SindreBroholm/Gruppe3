package com.fastis.controllers;

import com.fastis.data.User;
import com.fastis.repositories.BoardRepository;
import com.fastis.repositories.EventRepository;
import com.fastis.repositories.UserRepository;
import com.fastis.repositories.UserRoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {



    private EventRepository eventRepository;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private BoardRepository boardRepository;

    public MainController(EventRepository eventRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, BoardRepository boardRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.boardRepository = boardRepository;
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    @ResponseBody
    public User currentUser(Principal principal) {
        String loggedIn = principal.getName();
        if (loggedIn != null) {
            User user = userRepository.findByEmail(loggedIn);
            return user;
        }
        return null;
    }


    @GetMapping("/")
    public String showHome(Principal principal) {
    if(principal != null) {

        Integer userID = currentUser(principal).getId();


       // System.out.println(userRoleRepository.findByUserId(userID));
        return "/home";
    }
    return "/home";
    }


}
