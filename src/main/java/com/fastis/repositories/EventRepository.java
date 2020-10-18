package com.fastis.repositories;

import com.fastis.data.Event;
import com.fastis.data.MembershipType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {
    Event findById(int id);

    List<Event> findAllById(int id);


    @Query("select e from Event e join UserRole ur on ur.boardId = e.board.id where ur.userId = ?1")
    List<Event> getAllUserEvents(int userId);
}
