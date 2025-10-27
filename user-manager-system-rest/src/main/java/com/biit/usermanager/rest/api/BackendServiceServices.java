package com.biit.usermanager.rest.api;

/*-
 * #%L
 * User Manager System (Rest)
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

import com.biit.server.rest.CreatedElementServices;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.converters.BackendServiceConverter;
import com.biit.usermanager.core.converters.models.BackendServiceConverterRequest;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.repositories.BackendServiceRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend-services")
public class BackendServiceServices extends CreatedElementServices<
        BackendService,
        String,
        BackendServiceDTO,
        BackendServiceRepository,
        BackendServiceProvider,
        BackendServiceConverterRequest,
        BackendServiceConverter,
        BackendServiceController
        > {

    public BackendServiceServices(BackendServiceController backendServiceController) {
        super(backendServiceController);
    }

}
