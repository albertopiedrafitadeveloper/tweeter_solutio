package com.alberto.twitter;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {WebSocketServletAutoConfiguration.class,
    AopAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class,
    EmbeddedWebServerFactoryCustomizerAutoConfiguration.class})
@EnableJpaRepositories("com.alberto.twitter")
public class TwitterApplication {

  public static void main(String[] args) {
    SpringApplication.run(TwitterApplication.class, args);
  }

}