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
    private LocalDateTimeHandler LDTH;

    public BoardController(EventRepository eventRepository, BoardRepository boardRepository,
                           UserRepository userRepository, AccessVerifier accessVerifier,
                           InviteByEmail emailInviter, UserRoleRepository userRoleRepository,
                           LocalDateTimeHandler LDTH) {
        this.eventRepository = eventRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.accessVerifier = accessVerifier;
        this.userRoleRepository = userRoleRepository;
        this.emailInviter = emailInviter;
        this.LDTH = LDTH;
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

        model.addAttribute("name", event.getName());
        model.addAttribute("dayOfWeekStart", LDTH.getDayOfWeek(event.getDatetime_from()));
        model.addAttribute("dayAndMonthStart", LDTH.getDayOfMonth(event.getDatetime_from()));
        model.addAttribute("dayOfWeekEnd", LDTH.getDayOfWeek(event.getDatetime_to()));
        model.addAttribute("dayAndMonthEnd", LDTH.getDayOfMonth(event.getDatetime_to()));
        model.addAttribute("hourAndMinStart", LDTH.getHourAndMin(event.getDatetime_from()));
        model.addAttribute("hourAndMinEnd", LDTH.getHourAndMin(event.getDatetime_to()));
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
        model.addAttribute("admin", false);
        if (accessVerifier.doesUserHaveAccess(principal, board, MembershipType.ADMIN)){
            model.addAttribute("admin", true);
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
            return "redirect:/";
        }
        model.addAttribute("boardId", boardId);
        return "inviteByEmail";
    }

    @PostMapping("/inviteByEmail/{boardId}")
    public String inviteByEmail(Principal principal, @RequestParam String email, @PathVariable Integer boardId){
        User user = accessVerifier.currentUser(principal);
        Board board = boardRepository.findById(boardId).get();
        emailInviter.sendInvite(board, email, user);
        return "redirect:/";
    }


    @PostMapping("/addevent/{boardId}")
    public String addOrEditEvent(@ModelAttribute Event event, @PathVariable Integer boardId, BindingResult br, Principal principal){
        if (!accessVerifier.doesUserHaveAccess(principal, boardRepository.findById(boardId).get(), MembershipType.LEADER)){
            return "redirect:/";
        }
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
    public String createBoard(@ModelAttribute Board board, Principal principal){
        User user = accessVerifier.currentUser(principal);
        boardRepository.save(board);
        UserRole ur = new UserRole(user.getId(), board.getId(), MembershipType.ADMIN, 1);
        userRoleRepository.save(ur);
        return "redirect:/boardHome/1";
    }


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
        return "search";
    }

    @GetMapping("/members/{boardId}/{toggle}")
    public String showMembers(Principal principal, @PathVariable Integer boardId, Model model, @PathVariable Boolean toggle){
        Board board = boardRepository.findById(boardId).get();
        if (!accessVerifier.doesUserHaveAccess(principal, board, MembershipType.LEADER)){
            return "home";
        }
        User user = accessVerifier.currentUser(principal);
        UserRole currentuser = userRoleRepository.findAllByUserIdAndBoardId(user.getId(), boardId);
        List<UserRole> listOfUR = userRoleRepository.getAllByBoardId(boardId);
        List<UserWithRole> userList = new ArrayList<>();

        populateUserList(listOfUR, userList, toggle);


        UserRole userRole = new UserRole();
        model.addAttribute("listOfUr", listOfUR);
        model.addAttribute("userrole", userRole);
        model.addAttribute("toggle", toggle);
        model.addAttribute("curentboardId", boardId);
        model.addAttribute("curentuser", currentuser);
        model.addAttribute("members", userList);
        return "members";
    }

    private void populateUserList(List<UserRole> listOfUR, List<UserWithRole> userList, boolean onlyPending) {
        for (UserRole userRole : listOfUR) {
            UserWithRole members = new UserWithRole();
            members.setUserRole(userRole);
            members.setUser(userRepository.findById(userRole.getUserId()).get());
            if (onlyPending){
                if (userRole.isPendingMember()){
                    userList.add(members);
                }
            } else {
                if (!userRole.getMembershipType().name.equals("follower"))
                userList.add(members);
            }
        }

    }

    @PostMapping("/members/{boardId}/{toggle}")
    public String updateMembers(Principal principal, @PathVariable Integer boardId,@ModelAttribute UserRole userRole, @PathVariable Boolean toggle){
        Board board = boardRepository.findById(boardId).get();
        if (!accessVerifier.doesUserHaveAccess(principal, board, MembershipType.LEADER)){
            return "home";
        }
        userRoleRepository.save(userRole);
        return "redirect:/members/"+boardId+"/"+toggle;
    }

}
