package com.fastis.controllers;

import com.fastis.data.Board;
import com.fastis.data.User;
import com.fastis.repositories.BoardRepository;
import com.fastis.repositories.UserRepository;
import com.fastis.security.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserHandlerController {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private BoardRepository boardRepository;


    public UserHandlerController(UserRepository repository, PasswordEncoder passwordEncoder, BoardRepository boardRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.boardRepository = boardRepository;
    }

    /*
     *
     *
     *       LOG IN
     *
     * */

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /*
     *
     *       SIGNUP
     *
     * */
    @GetMapping("/signup")
    public String signup(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "signup";
    }

    @PostMapping("/signup")
    public String creatUser(@ModelAttribute User user, BindingResult br) {
        UserValidator validation = new UserValidator();
        if (validation.supports(user.getClass())) {
            validation.validate(user, br);
        }
        if (br.hasErrors()) {
            return "signup";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            repository.save(user);
            return "login";
        }
    }

    //
    // Myprofile
    //

    @GetMapping("/myprofile")
    public String myprofile() {
       return "myprofile";
    }







    /*


    BOARDHOME
     */
@GetMapping("/boardHome/{boardId}")
    public String boardHome(Model model, @PathVariable Integer boardId) {
    Board board = boardRepository.findById(boardId).get();
    model.addAttribute("currentBoard",board);

    return "boardHomeView";
}



}
