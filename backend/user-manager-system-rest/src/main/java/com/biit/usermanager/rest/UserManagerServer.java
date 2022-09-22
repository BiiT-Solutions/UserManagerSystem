package com.biit.usermanager.rest;


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
@ComponentScan({"com.biit.usermanager", "com.biit.server", "com.biit.server.security", "com.biit.messagebird.client"})
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

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
//			}
//		};
//	}
//
//	@Bean("threadPoolExecutor")
//	public TaskExecutor getAsyncExecutor() {
//		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setCorePoolSize(20);
//		executor.setMaxPoolSize(100);
//		executor.setWaitForTasksToCompleteOnShutdown(true);
//		executor.setThreadNamePrefix("Rest_Async-");
//		return executor;
//	}

	@Bean
	public ApplicationListener<ContextRefreshedEvent> startupLoggingListener() {
		return event -> UserManagerLogger.info(UserManagerServer.class, "### Server started ###");
	}
}
