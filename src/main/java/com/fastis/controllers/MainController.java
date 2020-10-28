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



    @GetMapping(value = {"/", "/{nextMonth}"})
    public String showAccessedBoards(Principal principal, Model model, @PathVariable(required = false) Integer nextMonth) {
        if (principal != null) {
            int currentMonth = LocalDateTime.now().getMonth().getValue();
            final LocalDateTime monthBlocker = LocalDateTime.now();
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

            User user = accessVerifier.currentUser(principal);
            List<Board> boardsList = accessVerifier.accessedBoard(principal);
            List<Event> boardEvents = new ArrayList<>();
            List<Event> filtedEvents = new ArrayList<>();
            List<Event> pagineringEvents = new ArrayList<>();

            for (Board b: boardsList) {
                boardEvents = eventRepository.EventStreamOrderByMonth(b.getId(), LocalDateTime.now());
                filtedEvents.addAll(accessVerifier.filterEvents(boardEvents, accessVerifier.getUserRole(user, b).getMembershipType()));
            }


            for (Event e : filtedEvents) {
                if (e.getDatetime_from().isAfter(LDTH.getFirstDayOfMonth(currentMonth))){
                    if (e.getDatetime_from().isBefore(LDTH.getLastDayOfMonth(currentMonth))){
                        pagineringEvents.add(e);
                    }
                } else if (e.getDatetime_to().isAfter(LDTH.getFirstDayOfMonth(currentMonth))){
                    if (e.getDatetime_from().isBefore(LDTH.getLastDayOfMonth(currentMonth))){
                        pagineringEvents.add(e);
                    }
                }
            }

            pagineringEvents.sort(Comparator.comparing(Event::getDatetime_from));

            String currentMonthHeader = LDTH.getCurrentMonth(currentMonth);
            model.addAttribute("CM", currentMonth);
            model.addAttribute("currentMonthHeader", currentMonthHeader);
            model.addAttribute("eventList", pagineringEvents);
            model.addAttribute("currentYear", LDTH.getYear());
            model.addAttribute("MonthNav", LDTH.getFirstDayOfMonth(currentMonth));
            model.addAttribute("BlockMonthNav", monthBlocker);

            return "home";
        }
        return "redirect:/login";
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
