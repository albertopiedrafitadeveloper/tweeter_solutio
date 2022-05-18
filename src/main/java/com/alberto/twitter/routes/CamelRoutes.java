package com.alberto.twitter.routes;

import com.alberto.twitter.processor.TweetProcessor;
import com.alberto.twitter.repository.TweetRepository;
import com.alberto.twitter.service.TwitterApiService;
import javax.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
class CamelRoutes extends RouteBuilder {

  @Value("${kafka.twitter.topic}")
  private final String twitterTopic;
  private final TweetProcessor tweetProcessor;
  private final TwitterApiService twitterApiService;

  @Override
  public void configure() {
    CamelContext context = new DefaultCamelContext();
    restConfiguration().contextPath("/camel")
        .port("8080")
        .enableCORS(true)
        .apiContextPath("/api-doc")
        .apiProperty("api.title", "Test REST API")
        .apiProperty("api.version", "v1")
        .apiProperty("cors", "true")
        .apiContextRouteId("doc-api")
        .component("servlet")
        .bindingMode(RestBindingMode.json)
        .dataFormatProperty("prettyPrint", "true");
    rest("/tweets").description("Tweets REST service").id("api-route")
        .get("/")
        .description("The list of all the tweets")
        .produces(MediaType.APPLICATION_JSON)
        .consumes(MediaType.APPLICATION_JSON)
        .to("direct:tweets");
    from("direct:tweets").routeId("tweets-api")
        .log("direct:tweets!!")
        .bean(TweetRepository.class, "findAll");
//    twitterApiService.streamTwitterFeed();
  }

}
