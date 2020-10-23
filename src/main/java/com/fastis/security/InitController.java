package com.fastis.security;

import com.fastis.data.*;
import com.fastis.datahandlers.InviteByEmail;
import com.fastis.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.github.javafaker.Faker;


@RestController
public class InitController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private BoardRepository boardRepository;
    private UserRoleRepository userRoleRepository;
    private EventRepository eventRepository;
    private NotificationRepository notificationRepository;
    private InviteByEmail inviteByEmail;

    private AccessVerifier accessVerifier;

    public InitController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          BoardRepository boardRepository, UserRoleRepository userRoleRepository,
                          EventRepository eventRepository, NotificationRepository notificationRepository,
                          InviteByEmail inviteByEmail, AccessVerifier accessVerifier
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.boardRepository = boardRepository;
        this.userRoleRepository = userRoleRepository;
        this.eventRepository = eventRepository;
        this.notificationRepository = notificationRepository;
        this.inviteByEmail = inviteByEmail;
        this.accessVerifier = accessVerifier;
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
        //settingUpNotifications(boards);

        return "ok";
    }

    @GetMapping("/testStuff")
    public String testStuff(){
        Board board = new Board();
        board.setName("MidtbyenIL");
        board.setContactName("Sindre Sæther");
        board.setContactNumber("93071137");
        board.setContactEmail("test@test.no");
        inviteByEmail.sendInvite(board,
                "",  new User("test0@test.no",
                        "Ola0", "Nordmann0",
                        passwordEncoder.encode("123"),
                        "0000")
        );
        return "OK";
    }

    public List<User> settingUpUsers() {
        User user0 = userRepository.findByEmail("test0@test.no");
        User user1 = userRepository.findByEmail("test1@test.no");
        User user2 = userRepository.findByEmail("test2@test.no");
        User user3 = userRepository.findByEmail("sindre@test.no");
        if (user0 == null) {
            user0 = new User("test0@test.no", "Ola0", "Nordmann0", passwordEncoder.encode("123"), "0000");
            userRepository.save(user0);
        }
        if (user1 == null) {
            user1 = new User("test1@test.no", "Ola1", "Nordmann1", passwordEncoder.encode("123"), "0000");
            userRepository.save(user1);
        }
        if (user2 == null) {
            user2 = new User("test2@test.no", "Ola2", "Nordmann2", passwordEncoder.encode("123"), "0000");
            userRepository.save(user2);
        }
        if (user2 == null) {
            user2 = new User("sindre@test.no", "Sindre Broholm", "Sæther", passwordEncoder.encode("123"), "93071137");
            userRepository.save(user3);
        }
        return Arrays.asList(user0, user1, user2, user3);
    }

    public List<Board> settingUpBoards() {
        Board board1 = boardRepository.findByName("TillerIL");
        Board board2 = boardRepository.findByName("MidtbyenIL");
        Board board3 = boardRepository.findByName("Rockheim");
        Board board4 = boardRepository.findByName("TrøndelagTeater");
        Board board5 = boardRepository.findByName("StrindheimIL");
        if (board1 == null) {
            board1 = new Board();
            board1.setName("TillerIL");
            board1.setContactName("Sindre Sæther");
            board1.setContactNumber("93071137");
            board1.setContactEmail("test@test.no");
            boardRepository.save(board1);
        }
        if (board2 == null) {
            board2 = new Board();
            board2.setName("MidtbyenIL");
            board2.setContactName("Sindre Sæther");
            board2.setContactNumber("93071137");
            board2.setContactEmail("MidtbyenIL@test.no");
            boardRepository.save(board2);
        }
        if (board3 == null) {
            board3 = new Board();
            board3.setName("Rockheim");
            board3.setContactName("Aage Aleksandersen");
            board3.setContactNumber("93071137");
            board3.setContactEmail("Rockheim@test.no");
            boardRepository.save(board3);
        }
        if (board4 == null) {
            board4 = new Board();
            board4.setName("TrøndelagTeater");
            board4.setContactName("Liv Ulmann");
            board4.setContactNumber("93071137");
            board4.setContactEmail("TrøndelagTeater@test.no");
            boardRepository.save(board4);
        }
        if (board5 == null) {
            board5 = new Board();
            board5.setName("StrindheimIL");
            board5.setContactName("Sindre Sæther");
            board5.setContactNumber("93071137");
            board5.setContactEmail("StrindheimIL@test.no");
            boardRepository.save(board5);
        }

        return Arrays.asList(board1, board2, board3, board4, board5);
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
    public String random() {
        Faker faker = new Faker();
        List<String> random = new ArrayList<>();
        random.add("FOLLOWER");
        random.add("MEMBER");
        random.add("LEADER");
        random.add("ADMIN");

        return random.get(faker.random().nextInt(0,3));
    }

    public List<Event> settingUpEvents(List<User> users, List<Board> boards) {

        List<Event> events = (List<Event>) eventRepository.findAll();

        if (true) {
            LocalDateTime today = LocalDateTime.now();

            Board board = boards.get((int)Math.ceil(Math.random() * 4));
            String message = "this is a message1 about an event";
            LocalDateTime start = today;
            LocalDateTime end = today;
           /* Event event1 = new Event(board, message,
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
                    "Trondheim", "Follower fest!");*/

            Faker faker = new Faker();

            //TillerIL events
            for (int i = 0; i < 15; i++){
                int add = faker.random().nextInt(0,30);
                start = today.plusDays(add);
                end = today.plusDays(30+add);
                events.add(new Event(boards.get(0),faker.chuckNorris().fact(), start, end, start, MembershipType.valueOf(random()) , faker.lordOfTheRings().location(), "Tiller vs " +faker.esports().team()));
            }
            //Midtbyen
            for (int i = 0; i < 15; i++){
                int add = (int)Math.ceil(Math.random() * 29 +1);
                int addFive = (int)Math.ceil(Math.random() * 4);
                start = today.plusDays(add);
                end = today.plusDays(30+add);
                events.add(new Event(boards.get(1),faker.chuckNorris().fact(), start, end, start, MembershipType.valueOf(random()) , faker.country().capital(), "Midtbyen vs " +faker.esports().team()));
            }
            //Rockheim
            for (int i = 0; i < 15; i++){
                int add = (int)Math.ceil(Math.random() * 29 +1);
                int addFive = (int)Math.ceil(Math.random() * 4);
                start = today.plusDays(add);
                end = today.plusDays(30+add);
                events.add(new Event(boards.get(2),faker.internet().domainName(), start, end, start, MembershipType.valueOf(random()) , "Internett", "Learn to play "+faker.music().instrument()+" online"));
            }
            //trøndelagTeater
            for (int i = 0; i < 15; i++){
                int add = (int)Math.ceil(Math.random() * 29 +1);
                int addFive = (int)Math.ceil(Math.random() * 4);
                start = today.plusDays(add);
                end = today.plusDays(30+add);
                events.add(new Event(boards.get(3),faker.harryPotter().quote(), start, end, start, MembershipType.valueOf(random()) , faker.harryPotter().location(), faker.harryPotter().house()));
            }
            //strindheim IL
            for (int i = 0; i < 15; i++){
                int add = (int)Math.ceil(Math.random() * 29 +1);
                int addFive = (int)Math.ceil(Math.random() * 4);
                start = today.plusDays(add);
                end = today.plusDays(30+add);
                events.add(new Event(boards.get(4), faker.chuckNorris().fact(), start, end, start, MembershipType.valueOf(random()) , faker.lordOfTheRings().location(), "Tiller vs " +faker.esports().team()));
            }

           /* events.addAll(Arrays.asList(event1, event2, event3, event4, event5));*/

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