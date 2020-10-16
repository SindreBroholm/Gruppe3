package com.fastis.repositories;

import com.fastis.data.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {
    Event findById(int id);

    List<Event> findAllById(int id);
}
