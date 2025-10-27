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
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.persistence.entities.Application;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConverter extends ElementConverter<Application, ApplicationDTO, ApplicationConverterRequest> {


    @Override
    protected ApplicationDTO convertElement(ApplicationConverterRequest from) {
        final ApplicationDTO applicationDTO = new ApplicationDTO();
        BeanUtils.copyProperties(from.getEntity(), applicationDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        return applicationDTO;
    }

    @Override
    public Application reverse(ApplicationDTO to) {
        if (to == null) {
            return null;
        }
        final Application application = new Application();
        BeanUtils.copyProperties(to, application, ConverterUtils.getNullPropertyNames(to));
        return application;
    }
}
