package org.test.consumer.service;

import lombok.AllArgsConstructor;
import org.apache.fineract.avro.BulkMessageItemV1;
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
            if(message.getType().equalsIgnoreCase("BulkBusinessEvent"))
            {
                Method methodToGetDatas = payLoad.getClass().getMethod("getDatas",null);
                List<BulkMessageItemV1> bulkMessages = ( List<BulkMessageItemV1>)methodToGetDatas.invoke(payLoad);
                StringBuilder bulkMessagePayload = new StringBuilder();
                for(BulkMessageItemV1 bulkMessage : bulkMessages)
                {
                    EventMessageDTO bulkMessageData = retreiveBulkMessage(bulkMessage);
                    bulkMessagePayload.append(bulkMessageData);
                    bulkMessagePayload.append(System.lineSeparator());
                }
                eventMessages.add(new EventMessageDTO(message.getEventId(), message.getType(), message.getCategory(), message.getTenantId(), message.getCreatedAt(), bulkMessagePayload.toString()));

            }
            else {
                eventMessages.add(new EventMessageDTO(message.getEventId(), message.getType(), message.getCategory(), message.getTenantId(), message.getCreatedAt(), payLoad.toString()));
            }
        }

        return eventMessages;
    }

    private EventMessageDTO retreiveBulkMessage(BulkMessageItemV1 messageItem) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class messageBulkMessagePayLoad = Class.forName(messageItem.getDataschema());
        Method methodForPayLoad = messageBulkMessagePayLoad.getMethod("fromByteBuffer", ByteBuffer.class);
        Object payLoadBulkItem =  methodForPayLoad.invoke(null,messageItem.getData());
        return new EventMessageDTO(messageItem.getId(), messageItem.getType(), messageItem.getCategory(), "", "", payLoadBulkItem.toString());
    }
}
