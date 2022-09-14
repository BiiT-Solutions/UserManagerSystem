package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.User;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdCard(String idCard);
    Optional<User> findByNameAndLastname(String Name, String Lastname);


    List<User> findAllByEnabled(Boolean enable);
}
