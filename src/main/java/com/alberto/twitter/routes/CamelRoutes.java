package com.alberto.twitter.routes;

import com.alberto.twitter.repository.TweetRepository;
import com.alberto.twitter.service.TwitterApiService;
import javax.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
class CamelRoutes extends RouteBuilder {

  private final TweetRepository tweetRepository;
  private final TwitterApiService twitterApiService;

  @Override
  public void configure() {

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
        .get("/all")
          .description("The list of all the tweets")
          .consumes(MediaType.APPLICATION_JSON)
          .produces(MediaType.APPLICATION_JSON)
          .route()
          .bean(TweetRepository.class, "findAll")
          .endRest()
        .get("/all/user/{id}/verified")
          .description("The list of all verified tweets from a user")
          .consumes(MediaType.APPLICATION_JSON)
          .produces(MediaType.APPLICATION_JSON)
          .route()
          .bean(TweetRepository.class, "findAllByTwitterUserAndVerifiedTrue(${header.id})")
          .endRest()
        .get("/all/used")
          .description("The most used tweets. If ?quantity queryString is informed, "
              + "results are limited to that value, otherwise limit is 10")
          .consumes(MediaType.APPLICATION_JSON)
          .produces(MediaType.APPLICATION_JSON)
          .route()
          .choice()
            .when(header("quantity").isNull())
              .bean(TweetRepository.class, "findMostUsed(10)")
            .otherwise()
              .bean(TweetRepository.class, "findMostUsed(${header.quantity})")
          .end()
          .endRest()
        .put("/tweet/{tweetId}/verified/")
          .description("Sets a tweet as verified")
          .consumes(MediaType.APPLICATION_JSON)
          .route()
          .process(exchange -> {
            String tweetId = exchange.getMessage().getHeader("tweetId").toString();
            tweetRepository.findByTweetId(tweetId).ifPresent(tweet ->{
              tweet.setVerified(true);
              tweetRepository.save(tweet);
            });
          })
          .endRest();


    new Thread(twitterApiService::streamTwitterFeed).start();

  }

}
