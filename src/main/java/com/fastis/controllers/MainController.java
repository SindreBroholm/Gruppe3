package com.fastis.controllers;

import com.fastis.data.*;
import com.fastis.datahandlers.LocalDateTimeHandler;
import com.fastis.repositories.BoardRepository;
import com.fastis.repositories.EventRepository;
import com.fastis.repositories.UserRepository;
import com.fastis.repositories.UserRoleRepository;
import com.fastis.security.AccessVerifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class MainController {


    private AccessVerifier accessVerifier;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private BoardRepository boardRepository;
    private LocalDateTimeHandler LDTH;


    public MainController(EventRepository eventRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, BoardRepository boardRepository, AccessVerifier accessVerifier, LocalDateTimeHandler LDTH) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.boardRepository = boardRepository;
        this.accessVerifier = accessVerifier;
        this.LDTH = LDTH;

    }

/*
*
* HOME VIEW
*
* */

    @GetMapping(value = {"/", "/{nextMonth}"})
    public String showAccessedBoards(Principal principal, Model model, @PathVariable(required = false) Integer nextMonth) {
        if (principal != null) {
            int pagination = LocalDateTime.now().getMonth().getValue();

            pagination = alterPagination(nextMonth, pagination);
            User user = accessVerifier.currentUser(principal);
            List<Board> allBoardsToUser = accessVerifier.accessedBoard(principal);
            List<Event> filteredEvents = getAllEventsFilterdByAccessTypeFromAllBoards(user, allBoardsToUser);
            List<Event> allEventsForCurrentMonth = getAllEventsForCurrentMonth(pagination, filteredEvents);

            model.addAttribute("currentMonth", pagination);
            model.addAttribute("currentMonthHeader", LDTH.getCurrentMonth(pagination));
            model.addAttribute("eventList", allEventsForCurrentMonth);
            model.addAttribute("currentYear", LDTH.getYear());
            model.addAttribute("MonthNav", LDTH.getFirstDayOfMonth(pagination));
            model.addAttribute("BlockMonthNav", LocalDateTime.now());

            return "home";
        }
        return "redirect:/login";
    }

    private int alterPagination(Integer nextMonth, int currentMonth) {
        int alterYear = 0;
        if (nextMonth != null){
            currentMonth = nextMonth;

            if (currentMonth == 13) {
                alterYear++;
                LDTH.setAlterYear(alterYear);
                LDTH.addYear();
                currentMonth = 1;
            }


            if (currentMonth == 0) {
                alterYear++;
                LDTH.setAlterYear(alterYear);
                LDTH.subtracktYear();
                currentMonth = 12;
            }
        }
        return currentMonth;
    }

    private List<Event> getAllEventsForCurrentMonth(int currentMonth, List<Event> filteredEvents) {
        List<Event> allEventsForCurrentMonth = new ArrayList<>();
        for (Event e : filteredEvents) {
            if (e.getDatetime_from().isAfter(LDTH.getFirstDayOfMonth(currentMonth))){
                if (e.getDatetime_from().isBefore(LDTH.getLastDayOfMonth(currentMonth))){
                    allEventsForCurrentMonth.add(e);
                }
            } else if (e.getDatetime_to().isAfter(LDTH.getFirstDayOfMonth(currentMonth))){
                if (e.getDatetime_from().isBefore(LDTH.getLastDayOfMonth(currentMonth))){
                    allEventsForCurrentMonth.add(e);
                }
            }
        }
        allEventsForCurrentMonth.sort(Comparator.comparing(Event::getDatetime_from));
        return allEventsForCurrentMonth;
    }

    private List<Event> getAllEventsFilterdByAccessTypeFromAllBoards(User user, List<Board> allBoardsToUser) {
        List<Event> allFilteredUsersEvents = new ArrayList<>();
        for (Board b: allBoardsToUser) {
            List<Event> allUsersEvents = eventRepository.EventStreamOrderByMonth(b.getId(), LocalDateTime.now());
            allFilteredUsersEvents.addAll(accessVerifier.filterEvents(allUsersEvents, accessVerifier.getUserRole(user, b).getMembershipType()));
        }
        return allFilteredUsersEvents;
    }

    @GetMapping("/userHomeView")
    public String userHomeView(){
        return "userHomeView";
    }

    @GetMapping("/boards")
    public String myBoards(Principal principal, Model model) {

        if (principal != null) {
            List<Board> boardsList = accessVerifier.accessedBoard(principal);
            model.addAttribute("boardsList", boardsList);
        }

        return "/boards";
    }

}
