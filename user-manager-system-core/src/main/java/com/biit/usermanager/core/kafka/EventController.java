package com.biit.usermanager.core.kafka;

import com.biit.kafka.events.Event;
import com.biit.kafka.logger.EventsLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Subscribes to the EventListener to obtain any event, and handles it.
 */
@Controller
public class EventController {


    public EventController(@Autowired(required = false) EventConsumerListener eventListener) {

        //Listen to topic
        if (eventListener != null) {
            eventListener.addListener((event, offset, groupId, key, partition, topic, timeStamp) ->
                    eventHandler(event, groupId, key, partition, topic, timeStamp));
        }
    }

    public void eventHandler(Event event, String groupId, String key, int partition, String topic, long timeStamp) {
        EventsLogger.debug(this.getClass(), "Received event '{}' on topic '{}', group '{}', key '{}', partition '{}' at '{}'",
                event, topic, groupId, key, partition, LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),
                        TimeZone.getDefault().toZoneId()));

//        final String createdBy = event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag()) != null
//                ? event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag())
//                : event.getCreatedBy();
    }
}
