package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserGroupUser;
import com.biit.usermanager.persistence.entities.UserGroupUserId;

import java.util.Set;

public interface UserGroupUserRepository extends StorableObjectRepository<UserGroupUser,
        UserGroupUserId> {

    Set<UserGroupUser> findByIdUserGroupId(Long userGroupId);

    Set<UserGroupUser> findByIdUserId(Long userId);
}
