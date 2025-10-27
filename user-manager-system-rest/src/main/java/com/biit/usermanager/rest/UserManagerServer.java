package com.biit.usermanager.rest;

/*-
 * #%L
 * User Manager System (Rest)
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


import com.biit.usermanager.logger.UserManagerLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@ComponentScan({"com.biit.usermanager", "com.biit.server", "com.biit.server.security", "com.biit.messagebird.client", "com.biit.kafka"})
@ConfigurationPropertiesScan({"com.biit.usermanager.rest"})
//@EnableJpaRepositories({"com.biit.usermanager.persistence.repositories"})
@EntityScan({"com.biit.usermanager.persistence.entities", "com.biit.server.security.userguard"})
public class UserManagerServer {

    public static void main(String[] args) {
        SpringApplication.run(UserManagerServer.class, args);
    }


    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LoggableDispatcherServlet();
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> startupLoggingListener() {
        return event -> UserManagerLogger.info(UserManagerServer.class, "### Server started ###");
    }

}
