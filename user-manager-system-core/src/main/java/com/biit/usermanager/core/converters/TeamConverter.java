package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.converters.models.TeamConverterRequest;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.persistence.entities.Team;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.stereotype.Component;

@Component
public class TeamConverter extends ElementConverter<Team, TeamDTO, TeamConverterRequest> {

    private final OrganizationConverter organizationConverter;
    private final OrganizationProvider organizationProvider;

    public TeamConverter(OrganizationConverter organizationConverter, OrganizationProvider organizationProvider) {
        this.organizationConverter = organizationConverter;
        this.organizationProvider = organizationProvider;
    }

    @Override
    protected TeamDTO convertElement(TeamConverterRequest from) {
        final TeamDTO teamDTO = new TeamDTO();
        BeanUtils.copyProperties(from.getEntity(), teamDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        if (from.getEntity().getParent() != null) {
            teamDTO.setParent(convertElement(new TeamConverterRequest(from.getEntity().getParent())));
        }

        try {
            //Converter can have the tournament defined already.
            if (from.getOrganization() != null) {
                teamDTO.setOrganization(organizationConverter.convert(
                        new OrganizationConverterRequest(from.getOrganization())));
            } else {
                teamDTO.setOrganization(organizationConverter.convert(
                        new OrganizationConverterRequest(from.getEntity().getOrganization())));
            }
        } catch (LazyInitializationException | FatalBeanException e) {
            teamDTO.setOrganization(organizationConverter.convert(
                    new OrganizationConverterRequest(organizationProvider.get(from.getEntity().getOrganization().getId()).orElse(null))));
        }
        return teamDTO;
    }

    @Override
    public Team reverse(TeamDTO to) {
        if (to == null) {
            return null;
        }
        final Team team = new Team();
        BeanUtils.copyProperties(to, team, ConverterUtils.getNullPropertyNames(to));
        if (to.getParent() != null) {
            team.setParent(reverse((to.getParent())));
        }
        if (to.getOrganization() != null) {
            team.setOrganization(organizationConverter.reverse(to.getOrganization()));
        }
        return team;
    }
}
