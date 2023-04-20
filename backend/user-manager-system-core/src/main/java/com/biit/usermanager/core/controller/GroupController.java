package com.biit.usermanager.core.controller;


import com.biit.server.controller.BasicInsertableController;
import com.biit.usermanager.core.converters.GroupConverter;
import com.biit.usermanager.core.converters.models.GroupConverterRequest;
import com.biit.usermanager.core.exceptions.GroupNotFoundException;
import com.biit.usermanager.core.providers.GroupProvider;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GroupController extends BasicInsertableController<Group, GroupDTO, GroupRepository,
        GroupProvider, GroupConverterRequest, GroupConverter> {

    @Autowired
    protected GroupController(GroupProvider provider, GroupConverter converter) {
        super(provider, converter);
    }

    @Override
    protected GroupConverterRequest createConverterRequest(Group entity) {
        return new GroupConverterRequest(entity);
    }

    public GroupDTO getByName(String name) {
        return converter.convert(new GroupConverterRequest(provider.findByName(name).orElseThrow(() -> new GroupNotFoundException(this.getClass(),
                "No Group with name '" + name + "' found on the system."))));
    }

    public List<GroupDTO> getGroupsWithoutParent() {
        return converter.convertAll(provider.findByParentIsNull().stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<GroupDTO> getGroupsWithParent() {
        return converter.convertAll(provider.findByParentIsNotNull().stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }


    public int deleteByName(String name) {
        return provider.deleteByName(name);
    }

}
