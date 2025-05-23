package org.test.consumer.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="event_message")
@Getter
@NoArgsConstructor
public class EventMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="event_id")
    private Long eventId;
    @Column(name="type")
    private String type;
    @Column(name="category")
    private String category;
    @Column(name="data_schema")
    private String schema;
    @Column(name="tenant_id")
    private String tenantId;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @Lob
    @Column(name="payload", columnDefinition="BLOB")
    private byte[] payload;
    @Column(name="business_date")
    private String businessDate;

    public EventMessage(Long eventId, String type, String category, String schema, String tenantId, LocalDateTime createdAt, byte[] payload, String businessDate) {
        this.eventId = eventId;
        this.type = type;
        this.category = category;
        this.schema = schema;
        this.tenantId = tenantId;
        this.createdAt = createdAt;
        this.payload = payload;
        this.businessDate = businessDate;
    }
}
