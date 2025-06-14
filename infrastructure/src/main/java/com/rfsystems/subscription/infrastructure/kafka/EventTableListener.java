package com.rfsystems.subscription.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rfsystems.subscription.infrastructure.json.Json;
import com.rfsystems.subscription.infrastructure.kafka.models.connect.MessageValue;
import com.rfsystems.subscription.infrastructure.kafka.models.event.EventMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EventTableListener {

    private static final Logger LOG = LoggerFactory.getLogger(EventTableListener.class);

    private static final TypeReference<MessageValue<EventMsg>> EVENT_MESSAGE_TYPE = new TypeReference<>() {
    };

    @KafkaListener(
            concurrency = "${kafka.consumers.events.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.events.topics}",
            groupId = "${kafka.consumers.events.group-id}",
            id = "${kafka.consumers.events.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.events.auto-offset-reset}"
            }
    )
    public void onMessage(@Payload(required = false) final String payload, final ConsumerRecordMetadata metadata) {
        if (payload == null) {
            LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: EMPTY", metadata.topic(), metadata.partition(), metadata.offset());
            return;
        }

        LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
        final var messagePayload = Json.readValue(payload, EVENT_MESSAGE_TYPE).payload();
        final var op = messagePayload.operation();

        if (!op.isCreate() && !op.isSnapshotRead()) {
            LOG.info("Event notification received from Kafka [topic:{}] [partition:{}] [offset:{}]: Discarding operation {}", metadata.topic(), metadata.partition(), metadata.offset(), op);
            return;
        }

        LOG.info("New event received from Kafka: {}", payload);
    }
}
