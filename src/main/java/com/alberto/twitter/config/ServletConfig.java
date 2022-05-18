package com.alberto.twitter.config;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

  @Bean
  public ServletRegistrationBean servletRegistrationBean() {
    ServletRegistrationBean servlet = new ServletRegistrationBean(new CamelHttpTransportServlet(),
        "/camel/*");
    servlet.setName("CamelServlet");
    return servlet;
  }

}
