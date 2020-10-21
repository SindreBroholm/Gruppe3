package com.fastis.controllers;

import com.fastis.data.Board;
import com.fastis.data.Event;
import com.fastis.data.User;
import com.fastis.data.UserRole;
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


    private int plussyear = 0;
    private int year = 2020;
    @GetMapping(value = {"/", "/{nextMonth}"})
    public String showAccessedBoards(Principal principal, Model model, @PathVariable(required = false) Integer nextMonth) {
        if (principal != null) {
            int month = LocalDateTime.now().getMonth().getValue();
            if (nextMonth != null){
                month = nextMonth;
                if (month == 13) {
                    plussyear++;
                    year++;
                    month = 1;
                }
                if (month == 0) {
                    plussyear--;
                    year--;
                    month = 12;
                }
            }
            model.addAttribute("CM", month);
            User user = accessVerifier.currentUser(principal);
            LocalDateTimeHandler LDTH = new LocalDateTimeHandler();
            LocalDateTime firstDayOfCurrentMonth = LDTH.getMonth(month, plussyear);
            LocalDateTime firstDayOfCNextMonth = LDTH.getMonth(month +1, plussyear);
            LocalDateTime today = LocalDateTime.now();
            List<Event> usersEvents = eventRepository.EventStreamOrderByMonth(user.getId(),firstDayOfCurrentMonth, firstDayOfCNextMonth, today);
            String currentMonth = LDTH.getCurrentMonth(month);

            model.addAttribute("month", currentMonth);
            model.addAttribute("eventList", usersEvents);
            model.addAttribute("currentYear", year);

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
