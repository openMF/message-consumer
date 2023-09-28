package org.test.consumer.job;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.test.consumer.domain.EventMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurgeOldEventsJob {
    private final EntityManager entityManager;

    @Scheduled(cron = "0 0 * * *")
    @Transactional
    public void purge() {
        log.info("Starting the purging");
        LocalDateTime todayStartOfDay = LocalDate.now(Clock.systemUTC()).atStartOfDay();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<EventMessage> deleteQ = cb.createCriteriaDelete(EventMessage.class);
        Root<EventMessage> from = deleteQ.from(EventMessage.class);
        deleteQ.where(cb.lessThan(from.get("createdAt"), todayStartOfDay));

        entityManager.createQuery(deleteQ).executeUpdate();
        log.info("Purging finished");
    }
}
