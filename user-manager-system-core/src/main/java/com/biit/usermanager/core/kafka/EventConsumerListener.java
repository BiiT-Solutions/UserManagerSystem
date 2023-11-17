package com.biit.usermanager.core.kafka;

import com.biit.kafka.consumers.EventListener;
import com.biit.kafka.events.Event;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

/***
 * Reads any event on a specific topic. Later will call the Controller to handle the event.
 */
@ConditionalOnExpression("!T(org.springframework.util.StringUtils).isEmpty('${spring.kafka.topic:}')")
@EnableKafka
@Configuration
public class EventConsumerListener extends EventListener {

    @Override
    @KafkaListener(topics = "${spring.kafka.topic:#{null}}", groupId = "${spring.kafka.group.id:null}", clientIdPrefix = "firstListener",
            containerFactory = "templateEventListenerContainerFactory")
    public void eventsListener(@Payload(required = false) Event event,
                               final @Header(KafkaHeaders.OFFSET) Integer offset,
                               final @Header(value = KafkaHeaders.KEY, required = false) String key,
                               final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timeStamp) {
        super.eventsListener(event, offset, key, partition, topic, timeStamp);
    }
}
