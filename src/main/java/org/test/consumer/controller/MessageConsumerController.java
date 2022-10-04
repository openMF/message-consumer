package org.test.consumer.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.test.consumer.data.EventMessageDTO;
import org.test.consumer.service.MessageConsumerService;
import java.util.List;

@RestController
@AllArgsConstructor
public class MessageConsumerController {

    private final MessageConsumerService service;

    @GetMapping("/consumer/getAllMessages")
    public List<EventMessageDTO> getMessages()
    {
        return service.getMessages();
    }

    @GetMapping("/consumer/getMessagesByEventType/{eventType}")
    public List<EventMessageDTO> getMessagesByType(@PathVariable("eventType") String eventType)
    {
        return service.getMessagesByType(eventType);
    }

    @DeleteMapping("/consumer/deleteAllMessages")
    public void deleteMessages()
    {
        service.deleteMessages();
    }

}
