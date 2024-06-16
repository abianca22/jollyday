package com.application.repository;

import com.application.model.Group;
import com.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>, CustomGroupRepository {
    Optional<Group> findByLeader(User leader);

    Optional<Group> findByName(String name);

}
