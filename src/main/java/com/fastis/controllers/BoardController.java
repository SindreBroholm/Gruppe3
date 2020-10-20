package com.fastis.controllers;

import com.fastis.data.*;
import com.fastis.datahandlers.InviteByEmail;
import com.fastis.repositories.BoardRepository;
import com.fastis.datahandlers.LocalDateTimeHandler;
import com.fastis.repositories.EventRepository;
import com.fastis.repositories.UserRepository;

import com.fastis.repositories.UserRoleRepository;
import com.fastis.security.AccessVerifier;
import com.fastis.validator.EventValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class BoardController {

    private EventRepository eventRepository;
    private BoardRepository boardRepository;
    private UserRepository userRepository;
    private AccessVerifier accessVerifier;
    private UserRoleRepository userRoleRepository;
    private InviteByEmail emailInviter;


    public BoardController(EventRepository eventRepository, BoardRepository boardRepository,
                           UserRepository userRepository, AccessVerifier accessVerifier,
                           InviteByEmail emailInviter, UserRoleRepository userRoleRepository) {
        this.eventRepository = eventRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.accessVerifier = accessVerifier;
        this.userRoleRepository = userRoleRepository;
        this.emailInviter = emailInviter;
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
            return "home";
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

         model.addAttribute("boardId", boardId);
        model.addAttribute("eventId", eventId);
        return "event";
    }


    @GetMapping(value={"/addevent/{boardId}", "/addevent/{boardId}/{eventId}"})
    public String showAddEvent(Model model, @PathVariable Integer boardId, @PathVariable(required = false) Integer eventId, Principal principal){

        User user = accessVerifier.currentUser(principal);
        Board board = boardRepository.findById(boardId).get();

        if (!accessVerifier.doesUserHaveAccess(principal, board, MembershipType.LEADER)){
            return "home";
        }

        //so we can add event to correct board
        model.addAttribute("boardId", boardId);

        Event event = new Event();
        if(eventId != null){
            event = eventRepository.findById(eventId).get();
        }
        model.addAttribute("event", event);

        return "eventform";
    }

    @GetMapping("/inviteByEmail/{boardId}")
    public String inviteByEmail(Model model, Principal principal, @PathVariable Integer boardId){
        User user = accessVerifier.currentUser(principal);
        Board board = boardRepository.findById(boardId).get();
        if (!accessVerifier.doesUserHaveAccess(principal, board, MembershipType.LEADER)){
            return "home";
        }
        model.addAttribute("boardId", boardId);
        return "inviteByEmail";
    }

    @PostMapping("/inviteByEmail/{boardId}")
    public String inviteByEmail(Principal principal, @RequestParam String email, @PathVariable Integer boardId){
        User user = accessVerifier.currentUser(principal);
        Board board = boardRepository.findById(boardId).get();
        emailInviter.sendInvite(board, email, user);
        return "home";
    }


    @PostMapping("/addevent/{boardId}")
    public String addOrEditEvent(@ModelAttribute Event event, @PathVariable Integer boardId, BindingResult br){
        EventValidator validator = new EventValidator();
        if(validator.supports(event.getClass())){
            validator.validate(event, br);
        }
        if(br.hasErrors()){
            return "eventform";
        }
        if(eventRepository.findById(event.getId()) == null){
            event.setBoard(boardRepository.findById(boardId).get());
        } else {
            Event oldEvent = eventRepository.findById(event.getId());
            oldEvent.setMessage(event.getMessage());
            oldEvent.setDatetime_from(event.getDatetime_from());
            oldEvent.setDatetime_to(event.getDatetime_to());
            oldEvent.setLocation(event.getLocation());
            oldEvent.setName(event.getName());
            oldEvent.setEvent_type(event.getEvent_type());
            event = oldEvent;
        }
        eventRepository.save(event);
        return "redirect:/event/" + event.getBoard().getId() + "/" + event.getId();
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

    @GetMapping("/members/{boardId}")
    public String showMembers(Principal principal, @PathVariable Integer boardId, Model model){
        Board board = boardRepository.findById(boardId).get();
        if (!accessVerifier.doesUserHaveAccess(principal, board, MembershipType.LEADER)){
            return "home";
        }
        User user = accessVerifier.currentUser(principal);
        UserRole currentuser = userRoleRepository.findAllByUserIdAndBoardId(user.getId(), boardId);
        List<UserRole> listOfUR = userRoleRepository.getAllByBoardId(boardId);
        List<UserWithRole> userList = new ArrayList<>();
        for (UserRole userRole : listOfUR) {
            UserWithRole userWithRole = new UserWithRole();
            userWithRole.setUserRole(userRole);
            userWithRole.setUser(userRepository.findById(userRole.getUserId()).get());
            userList.add(userWithRole);
        }

        UserRole userRole = new UserRole();
        model.addAttribute("listOfUr", listOfUR);
        model.addAttribute("userrole", userRole);
        model.addAttribute("curentboardId", boardId);
        model.addAttribute("curentuser", currentuser);
        model.addAttribute("members", userList);
        return "members";
    }

    @PostMapping("/members/{boardId}")
    public String updateMembers(Principal principal, @PathVariable Integer boardId,@ModelAttribute UserRole userRole){
        Board board = boardRepository.findById(boardId).get();
        if (!accessVerifier.doesUserHaveAccess(principal, board, MembershipType.LEADER)){
            return "home";
        }
        userRoleRepository.save(userRole);
        return "redirect:/members/"+boardId;
    }

}
