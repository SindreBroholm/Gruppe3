package com.fastis.controllers;

import com.fastis.data.User;
import com.fastis.repositories.UserRepository;
import com.fastis.security.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;


@Controller
public class UserHandlerController {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public UserHandlerController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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


    /*
     *
     *
     *           PROFILE
     *
     * */
    @GetMapping("/profile")
    public String viewProfile() {

        return "profile";
    }



    /*


    BOARDHOME
     */
@GetMapping("/boardHome")
    public String boardHome(){
    return "boardHomeView";
}

}
