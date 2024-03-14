package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.TeamConverter;
import com.biit.usermanager.core.converters.models.TeamConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationNotFoundException;
import com.biit.usermanager.core.exceptions.TeamNotFoundException;
import com.biit.usermanager.core.kafka.TeamEventSender;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.TeamProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TeamController extends KafkaElementController<Team, Long, TeamDTO, TeamRepository,
        TeamProvider, TeamConverterRequest, TeamConverter> {

    private final ApplicationConverter applicationConverter;
    private final ApplicationProvider applicationProvider;

    @Autowired
    protected TeamController(TeamProvider provider, TeamConverter converter, ApplicationConverter applicationConverter,
                             ApplicationProvider applicationProvider, TeamEventSender eventSender) {
        super(provider, converter, eventSender);
        this.applicationConverter = applicationConverter;
        this.applicationProvider = applicationProvider;
    }

    @Override
    protected TeamConverterRequest createConverterRequest(Team entity) {
        return new TeamConverterRequest(entity);
    }

    public TeamDTO getByName(String name, String applicationName) {
        final Application application = applicationProvider.findByName(applicationName).orElseThrow(() ->
                new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."));
        return getConverter().convert(new TeamConverterRequest(getProvider().findByNameAndApplication(name, application)
                .orElseThrow(() -> new TeamNotFoundException(this.getClass(), "No Team with name '" + name + "' found on the system.")), application));
    }

    public TeamDTO getByName(String name, ApplicationDTO applicationDTO) {
        return getConverter().convert(new TeamConverterRequest(getProvider().findByNameAndApplication(name, applicationConverter.reverse(applicationDTO))
                .orElseThrow(() -> new TeamNotFoundException(this.getClass(), "No Team with name '" + name + "' found on the system."))));
    }

    public List<TeamDTO> getTeamsWithoutParent() {
        return getConverter().convertAll(getProvider().findByParentIsNull().stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<TeamDTO> getTeamsWithParent() {
        return getConverter().convertAll(getProvider().findByParentIsNotNull().stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }


    public int deleteByName(String name, String applicationName) {
        final Application application = applicationProvider.findByName(applicationName).orElseThrow(() ->
                new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."));
        return getProvider().deleteByName(name, application);
    }

}
