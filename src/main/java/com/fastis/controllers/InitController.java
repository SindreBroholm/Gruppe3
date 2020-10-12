package com.fastis.controllers;

import com.fastis.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public InitController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/init")
    public String init(){
        User user = userRepository.findByEmail("test@test.no");
        if(user == null){
            user = new User("test@test.no", "Ola", "Nordmann", passwordEncoder.encode("123"));
            userRepository.save(user);
        }
        return "ok";
    }
}
