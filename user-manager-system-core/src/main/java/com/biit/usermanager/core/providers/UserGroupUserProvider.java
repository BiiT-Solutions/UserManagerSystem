package com.biit.usermanager.core.providers;

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.persistence.entities.UserGroupUser;
import com.biit.usermanager.persistence.entities.UserGroupUserId;
import com.biit.usermanager.persistence.repositories.UserGroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserGroupUserProvider extends StorableObjectProvider<UserGroupUser, UserGroupUserId, UserGroupUserRepository> {


    @Autowired
    public UserGroupUserProvider(UserGroupUserRepository repository) {
        super(repository);
    }

    public Set<UserGroupUser> findByIdUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public Set<UserGroupUser> findByIdUserGroupId(Long userGroupId) {
        return getRepository().findByIdUserGroupId(userGroupId);
    }
}
