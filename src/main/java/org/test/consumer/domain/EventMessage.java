package org.test.consumer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="event_message")
@Getter
@NoArgsConstructor
public class EventMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="event_id")
    private int eventId;
    @Column(name="type")
    private String type;
    @Column(name="category")
    private String category;
    @Column(name="data_schema")
    private String schema;
    @Column(name="tenant_id")
    private String tenantId;
    @Column(name="created_at")
    private String createdAt;
    @Lob
    @Column(name="payload", columnDefinition="BLOB")
    private byte[] payload;

    public EventMessage(int eventId, String type, String category, String schema, String tenantId, String createdAt, byte[] payload) {
        this.eventId = eventId;
        this.type = type;
        this.category = category;
        this.schema = schema;
        this.tenantId = tenantId;
        this.createdAt = createdAt;
        this.payload = payload;
    }
}
