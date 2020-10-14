package com.fastis.security;

import com.fastis.data.Board;
import com.fastis.data.MembershipType;
import com.fastis.data.User;
import com.fastis.data.UserRole;
import com.fastis.repositories.BoardRepository;
import com.fastis.repositories.UserRepository;
import com.fastis.repositories.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
public class InitController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private BoardRepository boardRepository;
    private UserRoleRepository userRoleRepository;

    public InitController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          BoardRepository boardRepository,
                          UserRoleRepository userRoleRepository
                          ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.boardRepository = boardRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @GetMapping("/init")
    public String init() {

        //Setting up users
        List<User> users = settingUpUsers();

        //Setting up boards
        List<Board> boards = settingUpBoards();

        //connecting user to boards
        connectingUsersToBoards(users, boards);

        //setting up events
        settingUpEvents();

        return "ok";
    }

    public List<User> settingUpUsers() {
        User user0 = userRepository.findByEmail("test0@test.no");
        User user1 = userRepository.findByEmail("test1@test.no");
        User user2 = userRepository.findByEmail("test2@test.no");
        if (user0 == null) {
            user0 = new User("test0@test.no", "Ola0", "Nordmann0", passwordEncoder.encode("123"));
            userRepository.save(user0);
        }
        if (user1 == null) {
            user1 = new User("test1@test.no", "Ola1", "Nordmann1", passwordEncoder.encode("123"));
            userRepository.save(user1);
        }
        if (user2 == null) {
            user2 = new User("test2@test.no", "Ola2", "Nordmann2", passwordEncoder.encode("123"));
            userRepository.save(user2);
        }
        return Arrays.asList(user0, user1, user2);
    }

    public List<Board> settingUpBoards() {
        Board board1 = boardRepository.findByName("TillerIL");
        Board board2 = boardRepository.findByName("MidtbyenIL");
        if (board1 == null) {
            board1 = new Board();
            board1.setName("TillerIL");
            boardRepository.save(board1);
        }
        if (board2 == null) {
            board2 = new Board();
            board2.setName("MidtbyenIL");
            boardRepository.save(board2);
        }
        return Arrays.asList(board1, board2);
    }

    public List<UserRole> connectingUsersToBoards(List<User> users, List<Board> boards){

        List<UserRole> userRoles = (List<UserRole>) userRoleRepository.findAll();

        if (userRoles == null || userRoles.size() == 0){
            //user0 admin board0
            UserRole userRole1 = new UserRole(users.get(0).getId(),
                    boards.get(0).getId(),
                    MembershipType.ADMIN,
                    0
            );
            //user0 follower board1
            UserRole userRole2 = new UserRole(users.get(0).getId(),
                    boards.get(1).getId(),
                    MembershipType.FOLLOWER,
                    0
            );
            //user1 admin board1
            UserRole userRole3 = new UserRole(users.get(1).getId(),
                    boards.get(1).getId(),
                    MembershipType.ADMIN,
                    0
            );
            //user1 member board0
            UserRole userRole4 = new UserRole(users.get(1).getId(),
                    boards.get(0).getId(),
                    MembershipType.MEMBER,
                    0
            );
            //user2 follower board0
            UserRole userRole5 = new UserRole(users.get(2).getId(),
                    boards.get(0).getId(),
                    MembershipType.FOLLOWER,
                    0
            );

            userRoles.addAll(Arrays.asList(
                    userRole1,
                    userRole2,
                    userRole3,
                    userRole4,
                    userRole5
                    ));
            for (UserRole role : userRoles){
                userRoleRepository.save(role);
            }
        }
        return userRoles;
    }

    public void settingUpEvents(){
        //TODO
        List<Board> boards = (List<Board>) boardRepository.findAll();
    }


}
