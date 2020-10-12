package com.fastis.accountManagement;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> { // JpaRepository???
    User findByUsername(String username);
}