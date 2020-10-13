package com.fastis.controllers;

import com.fastis.data.Board;
import com.fastis.data.Event;
import com.fastis.repositories.BoardRepository;
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
    private BoardRepository boardRepository;

    public BoardController(EventRepository eventRepository,BoardRepository boardRepository) {
        this.eventRepository = eventRepository;
        this.boardRepository = boardRepository;
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
}
