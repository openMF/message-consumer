package org.test.consumer.service;

import org.test.consumer.data.EventMessageDTO;
import org.test.consumer.domain.EventMessage;

import java.util.List;

public interface MessageConsumerService {

    List<EventMessageDTO> getMessages();

    List<EventMessageDTO> getMessagesByType(String eventType);

    void deleteMessages();
}
