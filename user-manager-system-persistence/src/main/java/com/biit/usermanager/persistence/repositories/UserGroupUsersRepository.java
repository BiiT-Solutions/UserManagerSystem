package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserGroupUsers;
import com.biit.usermanager.persistence.entities.UserGroupUsersId;

import java.util.Set;

public interface UserGroupUsersRepository extends StorableObjectRepository<UserGroupUsers,
        UserGroupUsersId> {

    Set<UserGroupUsers> findByIdUserGroupId(Long userGroupId);

    Set<UserGroupUsers> findByIdUserId(Long userId);
}
