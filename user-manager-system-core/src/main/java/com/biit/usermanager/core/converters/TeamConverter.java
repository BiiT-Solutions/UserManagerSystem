package com.biit.usermanager.core.converters;

/*-
 * #%L
 * User Manager System (core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.converters.models.TeamConverterRequest;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.core.providers.TeamProvider;
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
    private final TeamProvider teamProvider;

    public TeamConverter(OrganizationConverter organizationConverter, OrganizationProvider organizationProvider,
                         TeamProvider teamProvider) {
        this.organizationConverter = organizationConverter;
        this.organizationProvider = organizationProvider;
        this.teamProvider = teamProvider;
    }

    @Override
    protected TeamDTO convertElement(TeamConverterRequest from) {
        final TeamDTO teamDTO = new TeamDTO();
        BeanUtils.copyProperties(from.getEntity(), teamDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        if (from.getEntity().getParent() != null) {
            teamDTO.setParentId(from.getEntity().getParent().getId());
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
            try {
                teamDTO.setOrganization(organizationConverter.convert(
                        new OrganizationConverterRequest(organizationProvider.get(from.getEntity().getOrganization().getId()).orElse(null))));
            } catch (LazyInitializationException e2) {
                //Organization name is not correctly retrieved as is Lazy. Search for the organization using the team.
                teamDTO.setOrganization(organizationConverter.convert(
                        new OrganizationConverterRequest(organizationProvider.findByTeam(from.getEntity()).orElse(null))));
            }
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
        if (to.getParentId() != null) {
            team.setParent(teamProvider.findById(to.getParentId()).orElse(null));
        }
        if (to.getOrganization() != null) {
            team.setOrganization(organizationConverter.reverse(to.getOrganization()));
        }
        return team;
    }
}
