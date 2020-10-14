package com.fastis.controllers;

import com.fastis.data.Event;
import com.fastis.datahandlers.LocalDateTimeHandler;
import com.fastis.repositories.EventRepository;
import com.fastis.validator.EventValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class BoardController {

    private EventRepository eventRepository;

    public BoardController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/event")
    public String showEvent(Model model){
         Event event = eventRepository.findById(5);

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
         //model.addAttribute("role", userRole.getMembershipType())
         return "event";
    }


    @GetMapping("/addevent")
    public String showAddEvent(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        return "eventform";
    }


    //Legg til funksjon for å redigere en eksisterende event!
    @PostMapping("/addevent")
    public String addOrEditEvent(@ModelAttribute Event event, BindingResult br){
        EventValidator validator = new EventValidator();
        if(validator.supports(event.getClass())){
            validator.validate(event, br);
        }
        if(br.hasErrors()){
            return "eventform";
        } else {
            eventRepository.save(event);
            return "redirect: /event";
        }
    }
}
