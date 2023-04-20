package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Group;

import java.util.Optional;

public class GroupConverterRequest extends ConverterRequest<Group> {
    public GroupConverterRequest(Group entity) {
        super(entity);
    }

    public GroupConverterRequest(Optional<Group> entity) {
        super(entity);
    }
}
