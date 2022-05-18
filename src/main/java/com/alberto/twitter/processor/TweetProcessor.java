package com.alberto.twitter.processor;

import com.alberto.twitter.domain.Tweet;
import com.alberto.twitter.repository.TweetRepository;
import com.alberto.twitter.util.JsonNodeUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TweetProcessor {

  private final TweetRepository tweetRepository;
  @Value("${twitter.minNumberOfFollowers}")
  private final Integer minNumberOfFollowers;
  @Value("${twitter.languages}")
  private final String languages;

  @SneakyThrows
  public void process(String payload) {
    JSONObject apiResponse = new JSONObject(payload);
    String tweetId = JsonNodeUtil.getJsonObject(apiResponse, "data", "id");
    tweetRepository.findByTweetId(tweetId).ifPresentOrElse(tweet -> {
      log.info("Tweet with id {} already saved", tweetId);
    }, () -> {
      saveTweet(tweetId, apiResponse);
    });
  }

  private void saveTweet(String tweetId, JSONObject apiResponse) {
    String language = JsonNodeUtil.getJsonObject(apiResponse, "data", "lang");
    Integer retweets = JsonNodeUtil.getJsonObject(apiResponse, "data", "public_metrics",
        "retweet_count");
//    String authorId = JsonNodeUtil.getJsonObject(apiResponse, "data", "author_id");
    String text = JsonNodeUtil.getJsonObject(apiResponse, "data", "text");
    JSONArray users = JsonNodeUtil.getJsonObject(apiResponse, "includes", "users");
    users.toList().stream().findFirst().ifPresent(hashMap -> {
      int followersCount = (int) ((HashMap) ((HashMap) hashMap).get("public_metrics")).get(
          "followers_count");
      String location = (String) ((HashMap) hashMap).get("location");
      String userName = (String) ((HashMap) hashMap).get("username");
      Boolean verified = (Boolean) ((HashMap) hashMap).get("verified");
      if (minNumberOfFollowers <= followersCount && Arrays.stream((languages.split(",")))
          .collect(Collectors.toList()).contains(language)) {
        Tweet entity = Tweet.builder().tweetId(tweetId).location(location).retweetCount(
                Long.valueOf(retweets))
            .twitterUser(userName).verified(verified)
            .text(Objects.requireNonNullElse(text, Strings.EMPTY)).build();
        tweetRepository.save(entity);
      }
    });
  }

}
