# User Manager Rest Server

### Rest project and Rest Server

As the rest classes are used by the Rest Client are in a specific package without the spring boot repackage. The Rest
module must be deployed as a standard one in Artifactory to be use as a test dependency by Rest Client module.

The Rest Server package, includes all spring boot configuration to be run as a server.

# Using User Manager Rest Client

If you want to access to this server, a client is provided to help you. Include on your mvn `pom.xml` file:

```
 <dependency>
       <groupId>com.biit</groupId>
       <artifactId>user-manager-system-rest-client</artifactId>
 </dependency>
```

Has a custom logger. Add its configuration to the `logback.xml` file:

```
    <logger name="com.biit.usermanager.logger.UserManagerClientLogger" additivity="false" level="DEBUG">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="DAILY"/>
    </logger>
```