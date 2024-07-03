package com.application.repository;

import com.application.model.Event;
import com.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, CustomEventRepository {
    List<Event> findAllByCollectorUser(User collector);
    List<Event> findAllByCelebratedUser(User celebratedUser);
    Optional<Event> findById(Integer id);
}
