package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventUtils;
import com.biit.usermanager.dto.OrganizationDTO;
import org.springframework.stereotype.Component;

@Component
public class OrganizationEventSender extends EventSender<OrganizationDTO> {

    private static final String EVENT_TYPE = "organizations";


    public OrganizationEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventUtils.TAG, EVENT_TYPE);
    }
}
