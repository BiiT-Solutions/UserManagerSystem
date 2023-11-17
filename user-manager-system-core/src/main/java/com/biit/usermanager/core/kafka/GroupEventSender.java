package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventUtils;
import com.biit.usermanager.dto.GroupDTO;
import org.springframework.stereotype.Component;

@Component
public class GroupEventSender extends EventSender<GroupDTO> {

    private static final String EVENT_TYPE = "groups";


    public GroupEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventUtils.TAG, EVENT_TYPE);
    }
}
