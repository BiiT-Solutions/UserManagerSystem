package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventTags;
import com.biit.usermanager.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamEventSender extends EventSender<TeamDTO> {

    private static final String EVENT_TYPE = "teams";


    public TeamEventSender(@Autowired(required = false) KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventTags.TEAM, EVENT_TYPE);
    }
}
