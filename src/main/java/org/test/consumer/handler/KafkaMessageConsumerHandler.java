package org.test.consumer.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.fineract.avro.MessageV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import org.test.consumer.domain.EventMessage;
import org.test.consumer.repository.EventMessageRepository;
import org.test.consumer.utility.ByteBufferConvertor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Consumes external Fineract events from Kafka and persists them.
 *
 * Mirrors the structure of {@link JMSMessageConsumerHandler}.
 */
@Component
@Slf4j
@Profile("kafka")
public class KafkaMessageConsumerHandler implements MessageHandler {

    @Autowired
    private ByteBufferConvertor byteBufferConvertor;

    @Autowired
    private EventMessageRepository repository;

    @Override
    @KafkaListener(
            topics = "${app.kafka.topic:external-events}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleMessage(Message<?> springMessage) throws MessagingException {
        byte[] rawPayload = (byte[]) springMessage.getPayload();
        ByteBuffer wrapperBuf = byteBufferConvertor.convert(rawPayload);

        try {
            MessageV1 messagePayload = MessageV1.fromByteBuffer(wrapperBuf);
            log.info("Received Kafka event of Category = {}, Type = {}",
                    messagePayload.getCategory(), messagePayload.getType());
            saveMessage(messagePayload);
        } catch (IOException e) {
            log.error("Unable to read message", e);
        }
    }

    private void saveMessage(MessageV1 messagePayload) {
        LocalDateTime createdAt =
                LocalDateTime.parse(messagePayload.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME);

        EventMessage message = new EventMessage(
                messagePayload.getId(),
                messagePayload.getType(),
                messagePayload.getCategory(),
                messagePayload.getDataschema(),
                messagePayload.getTenantId(),
                createdAt,
                byteBufferConvertor.convert(messagePayload.getData()),
                messagePayload.getBusinessDate()
        );
        repository.save(message);
    }
}
