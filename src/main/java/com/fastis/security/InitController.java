package com.fastis.security;

import com.fastis.data.*;
import com.fastis.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@RestController
public class InitController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private BoardRepository boardRepository;
    private UserRoleRepository userRoleRepository;
    private EventRepository eventRepository;
    private NotificationRepository notificationRepository;

    public InitController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          BoardRepository boardRepository,
                          UserRoleRepository userRoleRepository,
                          EventRepository eventRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.boardRepository = boardRepository;
        this.userRoleRepository = userRoleRepository;
        this.eventRepository = eventRepository;
        this.notificationRepository = notificationRepository;
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
        settingUpEvents(users, boards);

        //setting up notifications
        settingUpNotifications(boards);

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

    public List<UserRole> connectingUsersToBoards(List<User> users, List<Board> boards) {

        List<UserRole> userRoles = (List<UserRole>) userRoleRepository.findAll();

        if (userRoles == null || userRoles.size() == 0) {
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
            for (UserRole role : userRoles) {
                userRoleRepository.save(role);
            }
        }
        return userRoles;
    }

    public List<Event> settingUpEvents(List<User> users, List<Board> boards) {

        List<Event> events = (List<Event>) eventRepository.findAll();

        if (events == null || events.size() == 0) {
            LocalDateTime today = LocalDateTime.now();

            //populating first board in list with events
            Board board = boards.get(0);
            String message = "this is a message1 about an event";
            LocalDateTime start = today;
            Event event1 = new Event(board, message,
                    start, start.plusDays(2),
                    start, MembershipType.MEMBER,
                    "Trondheim", "Oppvarming til Trønderferst!");

            message = "this is a message2 about an event";
            start = today.plusDays(1);
            Event event2 = new Event(board, message,
                    start, start.plusDays(2),
                    start, MembershipType.MEMBER,
                    "Trondheim", "Trønderferst!");

            message = "this is a message3 about an event";
            start = today.plusDays(7);
            Event event3 = new Event(board, message,
                    start, start,
                    start, MembershipType.MEMBER,
                    "Trondheim", "Trøndernach!");

            message = "this is a message4 about an event";
            start = today.plusDays(14);
            Event event4 = new Event(board, message,
                    start, start.plusDays(2),
                    start, MembershipType.FOLLOWER,
                    "Trondheim", "Opprydding!");

            message = "this is a message5 about an event";
            start = today.plusDays(21);
            Event event5 = new Event(board, message,
                    start, start,
                    start, MembershipType.FOLLOWER,
                    "Trondheim", "Follower fest!");

            events.addAll(Arrays.asList(event1, event2, event3, event4, event5));

            for (Event event : events) {
                eventRepository.save(event);
            }
        }
        return events;
    }

    public void settingUpNotifications(List<Board> boards) {
        List<Notification> notifications = (List<Notification>) notificationRepository.findAll();

        if (notifications == null || notifications.size() == 0) {

            LocalDateTime today = LocalDateTime.now();

            //setting up notifications for first board in list
            Board board = boards.get(0);
            String message = "This is a very important notification";
            LocalDateTime created = today;
            Notification notification1 = new Notification(
                    board, message,
                    created, MembershipType.MEMBER
                    );

            message = "This is not a very important notification";
            created = today.minusDays(2);
            Notification notification2 = new Notification(
                    board, message,
                    created, MembershipType.MEMBER
            );

            message = "This is something else";
            created = today.minusDays(3);
            Notification notification3 = new Notification(
                    board, message,
                    created, MembershipType.FOLLOWER
            );

            message = "This is a very strange follwoer notification!";
            created = today.minusDays(7);
            Notification notification4 = new Notification(
                    board, message,
                    created, MembershipType.FOLLOWER
            );

            notifications.addAll(Arrays.asList(
                    notification1,
                    notification2,
                    notification3,
                    notification4
            ));

            for (Notification notification : notifications){
                notificationRepository.save(notification);
            }

        }
    }


}