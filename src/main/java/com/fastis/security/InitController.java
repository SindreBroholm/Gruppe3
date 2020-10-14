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
        User user0 = userRepository.findByEmail("test0@test.no");
        User user1 = userRepository.findByEmail("test1@test.no");
        User user2 = userRepository.findByEmail("test2@test.no");
        if(user0 == null ){
            user0 = new User("test0@test.no", "Ola0", "Nordmann0", passwordEncoder.encode("123"));
            userRepository.save(user0);
        }
        if(user1 == null ){
            user1 = new User("test1@test.no", "Ola1", "Nordmann1", passwordEncoder.encode("123"));
            userRepository.save(user1);
        }
        if(user2 == null ){
            user2 = new User("test2@test.no", "Ola2", "Nordmann2", passwordEncoder.encode("123"));
            userRepository.save(user2);
        }

        Board board1 = boardRepository.findByName("TillerIL");
        Board board2 = boardRepository.findByName("MidtbyenIL");
        if(board1 == null){
            board1 = new Board();
            board1.setName("TillerIL");
            boardRepository.save(board1);
        }
        if(board2 == null){
            board2 = new Board();
            board2.setName("TillerIL");
            boardRepository.save(board2);
        }
        return "ok";
    }
}
