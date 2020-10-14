package com.fastis.security;

import com.fastis.data.Board;
import com.fastis.data.User;
import com.fastis.repositories.BoardRepository;
import com.fastis.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private BoardRepository boardRepository;

    public InitController(UserRepository userRepository, PasswordEncoder passwordEncoder, BoardRepository boardRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.boardRepository = boardRepository;
    }

    @GetMapping("/init")
    public String init(){
        User user = userRepository.findByEmail("test@test.no");
        if(user == null){
            user = new User("test@test.no", "Ola", "Nordmann", passwordEncoder.encode("123"));
            userRepository.save(user);
        }

        Board board = boardRepository.findByName("TillerIL");
        if(board == null){
            board = new Board();
            board.setName("TillerIL");
            boardRepository.save(board);
        }

        return "ok";
    }
}
