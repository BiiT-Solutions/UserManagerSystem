package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserGroupUser;
import com.biit.usermanager.persistence.entities.UserGroupUserId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Transactional
public interface UserGroupUserRepository extends StorableObjectRepository<UserGroupUser,
        UserGroupUserId> {

    Set<UserGroupUser> findByIdUserGroupId(Long userGroupId);

    Set<UserGroupUser> findByIdUserId(Long userId);
}
