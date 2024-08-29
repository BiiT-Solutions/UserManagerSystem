package com.biit.usermanager.core.providers;

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.entities.UserGroupUser;
import com.biit.usermanager.persistence.entities.UserGroupUserId;
import com.biit.usermanager.persistence.repositories.UserGroupRepository;
import com.biit.usermanager.persistence.repositories.UserGroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserGroupUserProvider extends StorableObjectProvider<UserGroupUser, UserGroupUserId, UserGroupUserRepository> {


    @Value("${user.default.role.group:#{null}}")
    private String defaultGroupName;

    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserGroupUserProvider(UserGroupUserRepository repository, UserGroupRepository userGroupRepository) {
        super(repository);
        this.userGroupRepository = userGroupRepository;
    }

    public Set<UserGroupUser> findByIdUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public Set<UserGroupUser> findByIdUserGroupId(Long userGroupId) {
        return getRepository().findByIdUserGroupId(userGroupId);
    }

    public void assignToDefaultGroup(User user) {
        if (defaultGroupName != null && !defaultGroupName.isBlank() && user != null) {
            final UserGroup defaultGroup = userGroupRepository.findByNameIgnoreCase(defaultGroupName).orElse(null);
            if (defaultGroup != null) {
                try {
                    getRepository().save(new UserGroupUser(defaultGroup.getId(), user.getId()));
                    UserManagerLogger.info(this.getClass(), "User '{}' added to default group '{}'.", user, defaultGroup);
                } catch (Exception e) {
                    UserManagerLogger.warning(this.getClass(), "User '{}' cannot be added to default group '{}'.", user, defaultGroup);
                }
            } else {
                UserManagerLogger.debug(this.getClass(), "No default group defined or non existing. Property value '{}'.", defaultGroupName);
            }
        }
    }
}
