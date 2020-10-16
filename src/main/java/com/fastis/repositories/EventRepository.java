package com.fastis.repositories;

import com.fastis.data.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {
    Event findById(int id);

    List<Event> findAllById(int id);

    @Query("select e from UserRole ur join Board b on b.id = ur.boardId join Event e on b.id = e.board.id where ur.userId = ?1 order by e.datetime_from")
    List<Event> getAllEventsByUserId(int id);
}
