package com.fastis.repositories;

import com.fastis.data.Board;
import com.fastis.data.Event;
import com.fastis.data.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board,Integer> {
    Board findByName(String name);


    @Query("SELECT b FROM Board b WHERE b.name like %?1%")
    List<Board> search(String search);
}
