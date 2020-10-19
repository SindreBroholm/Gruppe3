package com.fastis.controllers;

import com.fastis.data.*;
import com.fastis.repositories.BoardRepository;
import com.fastis.datahandlers.LocalDateTimeHandler;
import com.fastis.repositories.EventRepository;
import com.fastis.repositories.UserRepository;

import com.fastis.security.AccessVerifier;
import com.fastis.validator.EventValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
public class BoardController {

    private EventRepository eventRepository;
    private BoardRepository boardRepository;
    private UserRepository userRepository;
    private AccessVerifier accessVerifier;


    public BoardController(EventRepository eventRepository,BoardRepository boardRepository, UserRepository userRepository, AccessVerifier accessVerifier) {
        this.eventRepository = eventRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.accessVerifier = accessVerifier;
    }


    /*


    BOARDHOME
     */
    @GetMapping("/boardHome/{boardId}")
    public String boardHome(Model model, @PathVariable Integer boardId, Principal principal) {
        Board board = boardRepository.findById(boardId).get();
        model.addAttribute("board",board);

        model.addAttribute("admin", false);

        MembershipType accesstype;
        if (principal != null){
            User user = accessVerifier.currentUser(principal);
            accesstype = accessVerifier.getUserRole(user, board).getMembershipType();
            if (accesstype == MembershipType.ADMIN){
                model.addAttribute("admin", true);
            }
        } else {
            System.out.println("Something else");
            accesstype = MembershipType.FOLLOWER;
        }

        List<Event> listOfEvents = accessVerifier.eventsForBoard(board);
        listOfEvents = accessVerifier.filterEvents(listOfEvents, accesstype);
        model.addAttribute("events", listOfEvents);

        return "boardHomeView";
    }

    @GetMapping("/event/{boardId}/{eventId}")
    public String showEvent(Model model, Principal principal, @PathVariable Integer boardId, @PathVariable Integer eventId){
        Board board = boardRepository.findById(boardId).get();
        Event event = eventRepository.findById(eventId).get();
        User user = accessVerifier.currentUser(principal);
        UserRole ur;

        if (accessVerifier.doesUserHaveAccess(principal, board, event.getEvent_type())){
            ur = accessVerifier.getUserRole(user, board);
        }else {
            return "redirect: home";
        }
         LocalDateTimeHandler localDateTimeHandler = new LocalDateTimeHandler();
         model.addAttribute("name", event.getName());
         model.addAttribute("dayOfWeekStart", localDateTimeHandler.getDayOfWeek(event.getDatetime_from()));
         model.addAttribute("dayAndMonthStart", localDateTimeHandler.getDayOfMonth(event.getDatetime_from()));
         model.addAttribute("dayOfWeekEnd", localDateTimeHandler.getDayOfWeek(event.getDatetime_to()));
         model.addAttribute("dayAndMonthEnd", localDateTimeHandler.getDayOfMonth(event.getDatetime_to()));
         model.addAttribute("hourAndMinStart", localDateTimeHandler.getHourAndMin(event.getDatetime_from()));
         model.addAttribute("hourAndMinEnd", localDateTimeHandler.getHourAndMin(event.getDatetime_to()));
         model.addAttribute("location", event.getLocation());
         model.addAttribute("description", event.getMessage());
         model.addAttribute("role", ur.getMembershipType().name);
         return "event";
    }


    @GetMapping("/addevent")
    public String showAddEvent(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        return "eventform";
    }


    @PostMapping("/addevent")
    public String addOrEditEvent(@ModelAttribute Event event, BindingResult br, @RequestParam(required = false) int id){
        EventValidator validator = new EventValidator();
        if(validator.supports(event.getClass())){
            validator.validate(event, br);
        }
        if(br.hasErrors()){
            return "eventform";
        }
        if(eventRepository.findById(id) != null){
            eventRepository.save(event);
            return "redirect: /event";
        }
        eventRepository.save(event);
        return "redirect: /event";
    }

    @GetMapping("/createboard")
    public String showCreateBoard(Model model){
        Board board = new Board();
        model.addAttribute("board", board);
        return "createnewboard";
    }

    @PostMapping("/createboard")
    public String createBoard(@ModelAttribute Board board, BindingResult br){
        EventValidator validator = new EventValidator();
        if(validator.supports(board.getClass())){
            validator.validate(board, br);
        }
        if(br.hasErrors()){
            return "createnewboard";
        } else {
            boardRepository.save(board);
            return "redirect: /board";
        }
    }

/*    @GetMapping("/profile")
    public String showUserProfile(Model model){
        *//*User user = userRepository.findById(1); //trenger hjelp her
        String username = user.getFirstname() + user.getLastname();
        model.addAttribute("user", user);*//*
        return "userprofile";
    }

    @GetMapping("/updateprofile")  //her vil vi sende inn user-infoen fra profile så bruker slipper fylle inn alle felter på nytt
    public String showUpdateProfile(){
        return "settingsprofile";
    }

    @PostMapping("/updateprofile")
    public String updateProfile(@ModelAttribute User user, BindingResult br){
        return "settingsprofile";
    }*/


    @GetMapping("/search")
    public String openSearch(){

        return "search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam() String keyword) {
        List<Board> searchResults;
        if (keyword != null) {
            searchResults = boardRepository.search(keyword);
        } else {
            searchResults = (List<Board>) boardRepository.findAll();
        }
        model.addAttribute("listProducts", searchResults);
        model.addAttribute("keyword", keyword);
        System.out.println(searchResults.toString());
        return "search";
    }
}
