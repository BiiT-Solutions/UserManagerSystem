package com.biit.usermanager.persistence.repositories;

/*-
 * #%L
 * User Manager System (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends ElementRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    List<User> findByUsernameIn(Collection<String> usernames);

    Optional<User> findByUsernameHash(String username);

    List<User> findByUsernameHashIn(Collection<String> usernames);

    List<User> findByEmailHash(String email);

    List<User> findByEmailHashIn(Collection<String> emails);

    List<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUuid(UUID uuid);

    List<User> findByUuidIn(Collection<UUID> uuid);

    List<User> findByUuidInAndCreatedOnHashIn(Collection<UUID> uuid, Collection<String> createdOn);

    List<User> findAllByAccountBlocked(boolean accountBlocked);

    long deleteByUsernameHash(String username);

    List<User> findByExternalReference(String externalReference);

    List<User> findByExternalReferenceIn(Collection<String> externalReference);
}
