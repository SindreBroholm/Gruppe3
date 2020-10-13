package com.fastis.controllers;

import com.fastis.data.Event;
import com.fastis.repositories.EventRepository;
import com.fastis.validator.EventValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;


@Controller
public class BoardController {

    private EventRepository eventRepository;

    public BoardController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/event")
    public String showEvent(){

        return "";
    }

    @GetMapping("/addevent")
    public String showAddEvent(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        return "eventform";
    }

    @PostMapping("/addevent")
    public String addEvent(@ModelAttribute Event event, BindingResult br){
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
