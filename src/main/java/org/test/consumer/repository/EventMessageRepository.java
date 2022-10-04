package org.test.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.consumer.domain.EventMessage;

import java.util.List;

public interface EventMessageRepository extends JpaRepository<EventMessage,Long> {
    List<EventMessage> findByType(String eventType);
}
