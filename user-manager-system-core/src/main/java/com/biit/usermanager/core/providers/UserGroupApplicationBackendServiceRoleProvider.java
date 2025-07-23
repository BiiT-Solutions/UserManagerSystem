package com.biit.usermanager.core.providers;

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.UserGroupApplicationBackendServiceRoleRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserGroupApplicationBackendServiceRoleProvider extends StorableObjectProvider<UserGroupApplicationBackendServiceRole,
        UserGroupApplicationBackendServiceRoleId, UserGroupApplicationBackendServiceRoleRepository> {

    public UserGroupApplicationBackendServiceRoleProvider(UserGroupApplicationBackendServiceRoleRepository repository) {
        super(repository);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByUserGroupId(Long userGroupId) {
        return getRepository().findByIdUserGroupId(userGroupId);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByUserGroupIdIn(Collection<Long> userGroupIds) {
        return getRepository().findByIdUserGroupIdIn(userGroupIds);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByApplicationName(String applicationName) {
        return getRepository().findByIdApplicationName(applicationName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByApplicationRoleName(String roleName) {
        return getRepository().findByIdRoleName(roleName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByBackendServiceName(String backendServiceName) {
        return getRepository().findByIdBackendServiceName(backendServiceName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByBackendServiceNameAndBackendServiceRole(String backendServiceName, String backendServiceRoleName) {
        return getRepository().findByIdBackendServiceNameAndIdBackendServiceRole(backendServiceName, backendServiceRoleName);
    }


    public Set<UserGroupApplicationBackendServiceRole> findByBackendServiceRole(String roleName) {
        return getRepository().findByIdBackendServiceRole(roleName);
    }

    public Optional<UserGroupApplicationBackendServiceRole> findBy(
            Long userId, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        return getRepository().findByIdUserGroupIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                userId, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findBy(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        return getRepository().findByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public void deleteBy(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        getRepository().deleteByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public List<UserGroupApplicationBackendServiceRole> findBy(
            Long userId, String applicationName, String applicationRoleName) {
        return getRepository().findByIdUserGroupIdAndIdApplicationNameAndIdRoleName(userId, applicationName, applicationRoleName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findBy(ApplicationRole applicationRole) {
        return findBy(applicationRole.getId().getApplication().getName(), applicationRole.getId().getRole().getName());
    }

    public Set<UserGroupApplicationBackendServiceRole> findBy(String applicationName, String applicationRoleName) {
        return getRepository().findByIdApplicationNameAndIdRoleName(applicationName, applicationRoleName);
    }

    public Optional<UserGroupApplicationBackendServiceRole> findBy(
            Long userId, ApplicationBackendServiceRole applicationBackendServiceRole) {
        return getRepository().findByIdUserGroupIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                userId, applicationBackendServiceRole.getId().getApplicationRole().getId().getApplication().getName(),
                applicationBackendServiceRole.getId().getApplicationRole().getId().getRole().getName(),
                applicationBackendServiceRole.getId().getBackendServiceRole().getId().getBackendService().getName(),
                applicationBackendServiceRole.getId().getBackendServiceRole().getId().getName()
        );
    }

}
