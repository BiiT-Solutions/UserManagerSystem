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