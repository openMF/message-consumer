package org.test.consumer.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.test.consumer.data.EventMessageDTO;
import org.test.consumer.domain.EventMessage;
import org.test.consumer.repository.EventMessageRepository;
import org.test.consumer.utility.ByteBufferConvertor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageConsumerServiceImpl implements MessageConsumerService{

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerServiceImpl.class);

    private final EventMessageRepository repository;
    private final ByteBufferConvertor byteBufferConvertor;

    @Override
    public List<EventMessageDTO> getMessages() {
        List<EventMessageDTO> eventMessages = new ArrayList<>();
        List<EventMessage> messages = repository.findAll();
        try {
            eventMessages = convertToReadableFormat(messages);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error("Unable to read message {}",e);
        }
        return eventMessages;
    }

    @Override
    public List<EventMessageDTO> getMessagesByType(String eventType) {
        List<EventMessageDTO> eventMessages = new ArrayList<>();
        List<EventMessage> messages = repository.findByType(eventType);
        try {
            eventMessages = convertToReadableFormat(messages);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.error("Unable to read message {}",e);
        }
        return eventMessages;
    }

    @Override
    public void deleteMessages() {
        repository.deleteAll();
    }

    private List<EventMessageDTO> convertToReadableFormat(List<EventMessage> messages) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        List<EventMessageDTO> eventMessages = new ArrayList<>();
        for(EventMessage message : messages)
        {
            Class payLoadClass = Class.forName(message.getSchema());
            ByteBuffer byteBuffer = byteBufferConvertor.convert(message.getPayload());
            Method method = payLoadClass.getMethod("fromByteBuffer", ByteBuffer.class);
            Object payLoad = method.invoke(null, byteBuffer);
            eventMessages.add(new EventMessageDTO(message.getEventId(),message.getType(), message.getCategory(), message.getTenantId(), message.getCreatedAt(),payLoad.toString()));
        }

        return eventMessages;
    }
}
