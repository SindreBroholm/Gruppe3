package com.fastis.controllers;

import com.fastis.data.Board;
import com.fastis.data.Event;
import com.fastis.data.User;
import com.fastis.data.UserRole;
import com.fastis.repositories.BoardRepository;
import com.fastis.repositories.EventRepository;
import com.fastis.repositories.UserRepository;
import com.fastis.repositories.UserRoleRepository;
import com.fastis.security.AccessVerifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {


    private AccessVerifier accessVerifier;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private BoardRepository boardRepository;

    public MainController(EventRepository eventRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, BoardRepository boardRepository, AccessVerifier accessVerifier) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.boardRepository = boardRepository;
        this.accessVerifier = accessVerifier;
    }


    @GetMapping("/")
    public String showAccessedBoards(Principal principal, Model model) {
        List<Board> boardsList = accessVerifier.accessedBoard(principal);
        //List<Event> eventList = accessVerifier.accessedEvents(principal);
        model.addAttribute("boardsList", boardsList);
        return "/home";
    }





}
