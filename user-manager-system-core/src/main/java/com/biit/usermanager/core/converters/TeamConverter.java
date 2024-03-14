package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.converters.models.TeamConverterRequest;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.persistence.entities.Team;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.stereotype.Component;

@Component
public class TeamConverter extends ElementConverter<Team, TeamDTO, TeamConverterRequest> {

    private final ApplicationConverter applicationConverter;
    private final ApplicationProvider applicationProvider;

    public TeamConverter(ApplicationConverter applicationConverter, ApplicationProvider applicationProvider) {
        this.applicationConverter = applicationConverter;
        this.applicationProvider = applicationProvider;
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
            if (from.getApplication() != null) {
                teamDTO.setApplication(applicationConverter.convert(
                        new ApplicationConverterRequest(from.getApplication())));
            } else {
                teamDTO.setApplication(applicationConverter.convert(
                        new ApplicationConverterRequest(from.getEntity().getApplication())));
            }
        } catch (LazyInitializationException | FatalBeanException e) {
            teamDTO.setApplication(applicationConverter.convert(
                    new ApplicationConverterRequest(applicationProvider.get(from.getEntity().getApplication().getId()).orElse(null))));
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
        if (to.getApplication() != null) {
            team.setApplication(applicationConverter.reverse(to.getApplication()));
        }
        return team;
    }
}
