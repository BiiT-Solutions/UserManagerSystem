package com.biit.usermanager.security;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.RoleDoesNotExistsException;
import com.biit.usermanager.security.exceptions.UserManagementException;

import java.util.Set;

public class AuthorizationService implements IAuthorizationService<Long, Long, Long> {
    @Override
    public Set<IUser<Long>> getAllUsers() throws UserManagementException {
        return null;
    }

    @Override
    public Set<IUser<Long>> getAllUsers(IGroup<Long> group) throws UserManagementException {
        return null;
    }

    @Override
    public IGroup<Long> getOrganization(Long organizationId) throws UserManagementException {
        return null;
    }

    @Override
    public IGroup<Long> getOrganization(String organizationName) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getAllAvailableOrganizations() throws UserManagementException {
        return null;
    }

    @Override
    public IRole<Long> getRole(Long aLong) throws UserManagementException, RoleDoesNotExistsException {
        return null;
    }

    @Override
    public IRole<Long> getRole(String roleName) throws UserManagementException, RoleDoesNotExistsException {
        return null;
    }

    @Override
    public Set<IActivity> getRoleActivities(IRole<Long> role) {
        return null;
    }

    @Override
    public Set<IRole<Long>> getUserGroupRoles(IGroup<Long> group) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserGroups(IUser<Long> user) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user, IGroup<Long> site) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IRole<Long>> getUserRoles(IUser<Long> user) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IRole<Long>> getUserRoles(IUser<Long> user, IGroup<Long> organization) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IRole<Long>> getAllRoles(IGroup<Long> organization) throws UserManagementException {
        return null;
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException {
        return false;
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity) throws UserManagementException {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public Set<IUser<Long>> getUsers(IRole<Long> role, IGroup<Long> organization) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserParentOrganizations(IUser<Long> user) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) throws UserManagementException {
        return null;
    }

    @Override
    public void addUserRole(IUser<Long> user, IRole<Long> role) throws UserManagementException {

    }

    @Override
    public void addUserOrganizationRole(IUser<Long> user, IGroup<Long> organization, IRole<Long> role) throws UserManagementException {

    }

    @Override
    public IRoleActivities getRoleActivities() {
        return null;
    }

    @Override
    public void setRoleActivities(IRoleActivities roleActivities) {

    }

    @Override
    public void createBeans() {

    }

    @Override
    public void cleanUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) {

    }
}
