package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.repositories.UserGroupRepository;
import com.biit.usermanager.persistence.repositories.UserGroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserGroupProvider extends ElementProvider<UserGroup, Long, UserGroupRepository> {

    private final UserGroupUserRepository userGroupUserRepository;

    @Autowired
    public UserGroupProvider(UserGroupRepository repository, UserGroupUserRepository userGroupUserRepository) {
        super(repository);
        this.userGroupUserRepository = userGroupUserRepository;
    }

    public Optional<UserGroup> findByName(String name) {
        return getRepository().findByNameIgnoreCase(name);
    }

    public long deleteByName(String name) {
        return getRepository().deleteByNameIgnoreCase(name);
    }


    public List<UserGroup> getByUserGroup(Long groupId) {
        return findByIdIn(userGroupUserRepository.findByIdUserGroupId(groupId).stream()
                .map(userGroupUsers -> userGroupUsers.getId().getUserGroupId()).toList());
    }

    public List<UserGroup> getByUser(Long userId) {
        return findByIdIn(userGroupUserRepository.findByIdUserId(userId).stream()
                .map(userGroupUsers -> userGroupUsers.getId().getUserGroupId()).toList());
    }
}
