package com.fastis.repositories;

import com.fastis.data.MembershipType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MembershipTypeRepository extends CrudRepository<MembershipType, String> {
}
