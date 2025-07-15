package com.biit.usermanager.client.providers;

import com.biit.server.security.IUserOrganizationProvider;
import com.biit.usermanager.dto.OrganizationDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Primary
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserOrganizationProvider implements IUserOrganizationProvider<OrganizationDTO> {

    @Override
    public OrganizationDTO findByName(String name) {
        return null;
    }

    @Override
    public Collection<OrganizationDTO> findByUsername(String username) {
        return List.of();
    }

    @Override
    public Collection<OrganizationDTO> findByUserUID(String uid) {
        return List.of();
    }
}
