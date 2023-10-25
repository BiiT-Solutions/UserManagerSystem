package com.biit.usermanager.core.controller;


import com.biit.server.controller.BasicElementController;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.GroupConverter;
import com.biit.usermanager.core.converters.models.GroupConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationNotFoundException;
import com.biit.usermanager.core.exceptions.GroupNotFoundException;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.GroupProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GroupController extends BasicElementController<Group, Long, GroupDTO, GroupRepository,
        GroupProvider, GroupConverterRequest, GroupConverter> {

    private final ApplicationConverter applicationConverter;
    private final ApplicationProvider applicationProvider;

    @Autowired
    protected GroupController(GroupProvider provider, GroupConverter converter, ApplicationConverter applicationConverter,
                              ApplicationProvider applicationProvider) {
        super(provider, converter);
        this.applicationConverter = applicationConverter;
        this.applicationProvider = applicationProvider;
    }

    @Override
    protected GroupConverterRequest createConverterRequest(Group entity) {
        return new GroupConverterRequest(entity);
    }

    public GroupDTO getByName(String name, String applicationName) {
        final Application application = applicationProvider.findByName(applicationName).orElseThrow(() ->
                new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."));
        return getConverter().convert(new GroupConverterRequest(getProvider().findByNameAndApplication(name, application)
                .orElseThrow(() -> new GroupNotFoundException(this.getClass(), "No Group with name '" + name + "' found on the system.")), application));
    }

    public GroupDTO getByName(String name, ApplicationDTO applicationDTO) {
        return getConverter().convert(new GroupConverterRequest(getProvider().findByNameAndApplication(name, applicationConverter.reverse(applicationDTO))
                .orElseThrow(() -> new GroupNotFoundException(this.getClass(), "No Group with name '" + name + "' found on the system."))));
    }

    public List<GroupDTO> getGroupsWithoutParent() {
        return getConverter().convertAll(getProvider().findByParentIsNull().stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<GroupDTO> getGroupsWithParent() {
        return getConverter().convertAll(getProvider().findByParentIsNotNull().stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }


    public int deleteByName(String name, String applicationName) {
        final Application application = applicationProvider.findByName(applicationName).orElseThrow(() ->
                new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."));
        return getProvider().deleteByName(name, application);
    }

}
