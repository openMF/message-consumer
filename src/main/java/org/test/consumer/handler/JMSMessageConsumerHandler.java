package org.test.consumer.handler;

import org.apache.fineract.avro.MessageV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import org.test.consumer.domain.EventMessage;
import org.test.consumer.repository.EventMessageRepository;
import org.test.consumer.utility.ByteBufferConvertor;

import java.io.IOException;
import java.nio.ByteBuffer;


@Component
public class JMSMessageConsumerHandler implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(JMSMessageConsumerHandler.class);

    @Autowired
    private ByteBufferConvertor byteBufferConvertor;

    @Autowired
    private EventMessageRepository repository;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException{
        ByteBuffer messageByteBuffer = byteBufferConvertor.convert((byte [] )message.getPayload());
        MessageV1 messagePayload = null;
        try {
            messagePayload = MessageV1.fromByteBuffer(messageByteBuffer);
        } catch (IOException e) {
            logger.error("Unable to read message {}",e);
        }
        logger.info("Received message for event of Category = {}, Type = {}",messagePayload.getCategory(), messagePayload.getType());
        saveMessage(messagePayload);
    }

    private void saveMessage(MessageV1 messagePayload){
        EventMessage message = new EventMessage(messagePayload.getId(),messagePayload.getType(),messagePayload.getCategory(),messagePayload.getDataschema(),messagePayload.getTenantId(),messagePayload.getCreatedAt(),byteBufferConvertor.convert(messagePayload.getData()));
        repository.save(message);
    }
}
