package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.TeamConverter;
import com.biit.usermanager.core.converters.models.TeamConverterRequest;
import com.biit.usermanager.core.exceptions.OrganizationNotFoundException;
import com.biit.usermanager.core.exceptions.TeamNotFoundException;
import com.biit.usermanager.core.kafka.TeamEventSender;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.core.providers.TeamProvider;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TeamController extends KafkaElementController<Team, Long, TeamDTO, TeamRepository,
        TeamProvider, TeamConverterRequest, TeamConverter> {

    private final OrganizationConverter organizationConverter;
    private final OrganizationProvider organizationProvider;

    @Autowired
    protected TeamController(TeamProvider provider, TeamConverter converter, OrganizationConverter organizationConverter,
                             OrganizationProvider organizationProvider, TeamEventSender eventSender) {
        super(provider, converter, eventSender);
        this.organizationConverter = organizationConverter;
        this.organizationProvider = organizationProvider;
    }

    @Override
    protected TeamConverterRequest createConverterRequest(Team entity) {
        return new TeamConverterRequest(entity);
    }

    public TeamDTO getByName(String name, String organizationName) {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() ->
                new OrganizationNotFoundException(this.getClass(), "Organization with name '" + organizationName + "' not found."));
        return getConverter().convert(new TeamConverterRequest(getProvider().findByNameAndOrganization(name, organization)
                .orElseThrow(() -> new TeamNotFoundException(this.getClass(), "No Team with name '" + name + "' found on the system.")), organization));
    }

    public TeamDTO getByName(String name, OrganizationDTO organizationDTO) {
        return getConverter().convert(new TeamConverterRequest(getProvider().findByNameAndOrganization(name, organizationConverter.reverse(organizationDTO))
                .orElseThrow(() -> new TeamNotFoundException(this.getClass(), "No Team with name '" + name + "' found on the system."))));
    }

    public List<TeamDTO> getTeamsWithoutParent() {
        return getConverter().convertAll(getProvider().findByParentIsNull().stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<TeamDTO> getTeamsWithParent() {
        return getConverter().convertAll(getProvider().findByParentIsNotNull().stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }


    public int deleteByName(String name, String organizationName) {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() ->
                new OrganizationNotFoundException(this.getClass(), "Organization with name '" + organizationName + "' not found."));
        return getProvider().deleteByName(name, organization);
    }

}
