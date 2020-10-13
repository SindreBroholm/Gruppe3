package com.fastis.repositories;

import com.fastis.data.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board,Integer> {
    Board findByName(String name);
}
