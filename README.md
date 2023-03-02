# User Manager Rest Server

# Architecture

The scope of `User Manager System` is to centralize the credentials and RBAC (Role Base Access Control) to any other
application that can be on the same server or on a different one.

Is a place where all roles and permissions are managed and can be edited by application.

## How it works

Let's see the next example:

![Architecture](documentation/UserManagerSystem.svg)

On this example we have different applications that are deployed on a different server. `Application A` wants to consume
a service that is on `Application B`. For this purpose, `Application A` sends its credentials for `Application B`. A
typical user and password. But `Application B` does not have the information to know if this user is allowed to access
or
not to its own API.

Then `Application B` send these credentials to the `User Manager System` that can also be on a different
server (`Server C`) and asks for the permissions on `Application B`. `User Manager System` validates the credentials and
return a list of roles for `Application B`. With this information `Application B` can generate its custom JWT to
authorize the communication between `Application A` and `Application B`.

Note that for the sake of simplicity of the example, we have omitted the connection between `Application B`
and `User Manager System`. `Application B` must also send its credentials to `User Manager System` and
receiving a JWT to allow the communication. These credentials are different that the ones provided by `Application A`.

Let's see now the same example, with more details:

![Components](documentation/UserManagerSystem%20-%20Components.svg)

`Application A` uses the library `Rest Server Security Client` for connecting to any other BiiT API. For this purpose,
reads the properties `jwt.user` and `jwt.password` from the `application.properties` that are required for
authentication purposes.

`Application B` that expose the API that `Application A` wants to consume, inherits the library `Rest Server Security`
where all authorization endpoints are defined. It includes the endpoints to obtain the JWT that will be used later
for the communication between A and B. To know if `Application A` can consume the API, it must check if the user
from `Application A` is authorized to access to them (has the relevant role). All the information about roles is on the
`User manager System`. Therefore, `Application B` must use the `User Manager System Rest Client` library to connects to
it and obtain the roles. But as the `User Manager System` also implements the `Rest Server Security` and requires a JWT
for accessing, `Application B` must also request it to `User Manager System` first (the blue arrows). This token (JWT B
on the example) will be used for any communication between `Server B` and `User Manager System` and therefore will be
used for getting the user's roles from `Application A`. Note that `Application B` has its custom
properties `jwt.user` and `jwt.password` that are related to the permissions from `Server B`.

## Remarks

Each application has its custom `jwt.user` and `jwt.password` in its `application.properties` and they must be different
between applications.

Roles definitions are only defined on the `User Manager System`. And shared among the BiiT ecosystem. Here we define
different behaviours between other applications, giving different permissions according the system needs. That means
that is possible that a correctly authenticated user does not have access to a specific API.

When generating the JWT for accessing to the `User Manager System`, also RBAC is checked, but is checked internally to
the `User Manager System` database. That means that is possible that a correctly authenticated user does not have access
to the `User Manager System` API.

### Rest module and Rest Server module

As the rest classes are used by the Rest Client are in a specific package without the spring boot repackage. The Rest
module must be deployed as a standard one in Artifactory to be use as a test dependency by Rest Client module.

The Rest Server module, includes all spring boot configuration that allows to be run as a server.

# Using User Manager Rest Client on an external application

If you want to access to this server, a client is provided to help you. It includes all basic information for
Authentication and Authorization using the User Manager Server.

Include on your mvn `pom.xml` file:

```
 <dependency>
       <groupId>com.biit</groupId>
       <artifactId>user-manager-system-rest-client</artifactId>
 </dependency>
```

And include the correct path on the ComponentScan:

```
@ComponentScan({"...", "com.biit.usermanager.client"})
```

On the `application.properties` set the next property:

```
usermanager.server.url=...
``` 

It has a custom logger. Add its configuration to the `logback.xml` file:

```
    <logger name="com.biit.usermanager.logger.UserManagerClientLogger" additivity="false" level="DEBUG">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="DAILY"/>
    </logger>
```

As it is using JWT for communication, remember to set the JWT settings on the `application.properties`:

```
jwt.user=
jwt.password=
```

## Testing an external application

If you want to test your application without connecting to the User Manager System, you can use the dependency

```
 <dependency>
       <groupId>com.biit</groupId>
       <artifactId>user-manager-system-test</artifactId>
       <scope>test</scope>
 </dependency>
```

This application include a basic AuthenticatedUserProvided that handle user on memory rather than accessing to the API.
For defining custom roles of your application, you need to add on the `application.properties` of your test the property:

```
user.provider.test.authorities=ADMIN,VIEWER
```

# Dependencies

As it uses `Rest Server Security`, all configurations for this library must be configured. Check the project for a
deeper information.

