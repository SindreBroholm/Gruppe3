package com.fastis.controllers;

import com.fastis.data.Board;
import com.fastis.data.Event;
import com.fastis.data.User;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

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
            int month = LocalDateTime.now().getMonth().getValue();
            int currentDate = LocalDateTime.now().getMonth().getValue();
            int plussyear = LDTH.getPlussyear();
            int year = LDTH.getYear();
            if (nextMonth != null){
                month = nextMonth;
                if (month == 13) {
                    plussyear++;
                    LDTH.setPlussyear(plussyear);
                    year++;
                    LDTH.setYear(year);
                    month = 1;
                }
                if (month == 0) {
                    plussyear--;
                    LDTH.setPlussyear(plussyear);
                    year--;
                    LDTH.setYear(year);
                    month = 12;
                }
            }
            model.addAttribute("CM", month);

            User user = accessVerifier.currentUser(principal);
            LocalDateTime firstDayOfCurrentMonth = LDTH.getMonth(month, plussyear);
            LocalDateTime firstDayOfCNextMonth = LDTH.getMonth(month +1, plussyear);
            LocalDateTime today = LocalDateTime.now();
            List<Event> usersEvents = eventRepository.EventStreamOrderByMonth(user.getId(),firstDayOfCurrentMonth, firstDayOfCNextMonth, today);
            String currentMonth = LDTH.getCurrentMonth(month);

            List<EventsByDate> eventsByDate = usersEvents.stream()
                    .collect(groupingBy(f -> f.getDatetime_from().toLocalDate())).entrySet().stream()
                    .map(k -> new EventsByDate(k.getKey(), k.getValue()))
                    .sorted(Comparator.comparing(d -> d.fromDate))
                    .collect(Collectors.toList());

            model.addAttribute("month", currentMonth);
            model.addAttribute("eventList", eventsByDate);
            model.addAttribute("currentYear", year);
            model.addAttribute("MonthNav", firstDayOfCurrentMonth);
            model.addAttribute("BlockMonthNav", LDTH.getMonth(currentDate, 0));

            return "home";
        }
        return "redirect:/login";
    }

    public class EventsByDate {
        public final LocalDate fromDate;
        public List<Event> events;

        public EventsByDate(LocalDate fromDate, List<Event> values) {
            this.fromDate = fromDate;
            this.events = values;
        }

        public LocalDate getFromDate() {
            return fromDate;
        }

        public String getDayNr() {
            return fromDate.format(DateTimeFormatter.ofPattern("dd"));
        }

        public String getDayName() {
            return fromDate.format(DateTimeFormatter.ofPattern("EEE"));
        }

        public String getMonth() {
            return fromDate.format(DateTimeFormatter.ofPattern("MM"));
        }

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
