package com.fastis.controllers;

import com.fastis.data.User;
import com.fastis.repositories.BoardRepository;
import com.fastis.repositories.UserRepository;
import com.fastis.security.AccessVerifier;
import com.fastis.security.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
public class UserHandlerController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private BoardRepository boardRepository;
    private AccessVerifier accessVerifier;


    public UserHandlerController(UserRepository repository, PasswordEncoder passwordEncoder, BoardRepository boardRepository, AccessVerifier accessVerifier) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.boardRepository = boardRepository;
        this.accessVerifier = accessVerifier;
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
            userRepository.save(user);
            return "login";
        }
    }

    //
    // Myprofile
    //

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = accessVerifier.currentUser(principal);
        model.addAttribute("user", user);
        return "profile";
    }


    @GetMapping("/settings")
    public String settings(Principal principal, Model model) {
        User user = accessVerifier.currentUser(principal);
        model.addAttribute("user", user);
        return "settingsprofile";
    }

    @PostMapping("/settings")
    public String swapcredientals(Principal principal, @ModelAttribute User user, Model model) {


        User currentUser = accessVerifier.currentUser(principal);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);


        model.addAttribute("user", currentUser);
        return "redirect:/profile";
    }


}
