package com.fastis.repositories;

import com.fastis.data.Event;
import com.fastis.data.MembershipType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {
    Event findById(int id);

    List<Event> findAllById(int id);

    @Query("select e from Event e join UserRole ur on ur.boardId = e.board.id where ur.userId = ?1 ORDER BY e.datetime_from")
    List<Event> getAllUserEvents(int userId);

    List<Event> findAllByBoardId(Integer boardId);

    @Query("select e from Event e join UserRole ur on ur.boardId = e.board.id where ur.boardId = ?1 " +
            "and e.datetime_from > ?2 and e.datetime_from < ?3  group by e.id order by e.datetime_from")
    List<Event> EventStreamOrderByMonth(int boardId, LocalDateTime today, LocalDateTime lastDayOfMonth);
}
//and e.datetime_from < ?3
//and e.datetime_to > ?3
