package com.fastis.security;

import com.fastis.data.*;
import com.fastis.repositories.BoardRepository;
import com.fastis.repositories.EventRepository;
import com.fastis.repositories.UserRepository;
import com.fastis.repositories.UserRoleRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccessVerifier {

    private UserRoleRepository userRoleRepository;
    private UserRepository userRepository;
    private BoardRepository boardRepository;
    private EventRepository eventRepository;

    public AccessVerifier(UserRoleRepository userRoleRepository, UserRepository userRepository, BoardRepository boardRepository, EventRepository eventRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.eventRepository = eventRepository;
    }


    public UserRole setUserRole(User user, Board board, MembershipType accessType) {
        UserRole userRole = userRoleRepository.findAllByUserIdAndBoardId(user.getId(), board.getId());
        if (userRole == null) {
            userRole = new UserRole(
                    user.getId(), board.getId(),
                    MembershipType.FOLLOWER, 0
            );
        }
        userRole.setMembershipType(accessType);
        return userRoleRepository.save(userRole);
    }

    //will return null if not at least a follower
    public UserRole getUserRole(User user, Board board) {
        return userRoleRepository.findAllByUserIdAndBoardId(user.getId(), board.getId());
    }

    //will return an empty list if not at least a follower at a single board
    public List<UserRole> getAllUserRoles(User user) {
        return userRoleRepository.findAllByUserId(user.getId());
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    @ResponseBody
    public User currentUser(Principal principal) {
        String loggedIn = principal.getName();
        if (loggedIn != null) {
            User user = userRepository.findByEmail(loggedIn);
            return user;
        }
        return null;
    }

    @RequestMapping
    @ResponseBody
    public List<Board> accessedBoard(Principal principal) {
            User user = currentUser(principal);
            List<UserRole> userRoleList = getAllUserRoles(user);
            List<Board> boardsList = new ArrayList<>();
            for (int i = 0; i < userRoleList.size(); i++) {
                Integer temp = userRoleList.get(i).getBoardId();
                Board board = boardRepository.findById(temp).get();
                boardsList.add(board);
            }
            return boardsList;
    }

    @RequestMapping
    @ResponseBody
    public List<Event> accessedEvents(Principal principal) {
        User user = currentUser(principal);
        List<Event> eventList =  eventRepository.getAllUserEvents(user.getId());
        return eventList;
    }

    public List<Event> eventsForBoard(Board board){
        return eventRepository.findAllByBoardId(board.getId());
    }

    public List<Event> filterEvents(List<Event> listOfEvents, User user) {

        return listOfEvents;
    }
}
