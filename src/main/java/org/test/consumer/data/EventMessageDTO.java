package org.test.consumer.data;

import lombok.Getter;

@Getter
public class EventMessageDTO {
    private final int eventId;
    private final String type;
    private final String category;
    private final String tenantId;
    private final String createdAt;
    private final String payLoad;

    public EventMessageDTO(int eventId, String type, String category, String tenantId, String createdAt, String payLoad) {
        this.eventId = eventId;
        this.type = type;
        this.category = category;
        this.tenantId = tenantId;
        this.createdAt = createdAt;
        this.payLoad = payLoad;
    }

    @Override
    public String toString() {
        return "{" +
                "eventId=" + eventId +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", payLoad='" + payLoad + '\'' +
                '}';
    }
}
