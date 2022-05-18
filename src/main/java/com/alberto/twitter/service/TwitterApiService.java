package com.alberto.twitter.service;


import com.alberto.twitter.processor.TweetProcessor;
import com.alberto.twitter.service.util.TwitterUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterApiService {

  private final TweetProcessor tweetProcessor;

  private static final String TWITTER_ENDPOINT = "https://api.twitter.com/2/tweets/sample/stream?expansions=author_id,referenced_tweets.id,in_reply_to_user_id,geo.place_id,attachments.media_keys,attachments.poll_ids,entities.mentions.username,referenced_tweets.id.author_id&tweet.fields=id,created_at,text,author_id,in_reply_to_user_id,referenced_tweets,attachments,withheld,geo,entities,public_metrics,possibly_sensitive,source,lang,context_annotations,conversation_id,reply_settings&user.fields=id,created_at,name,username,protected,verified,withheld,profile_image_url,location,url,description,entities,pinned_tweet_id,public_metrics&media.fields=media_key,duration_ms,height,preview_image_url,type,url,width,public_metrics,alt_text,variants&place.fields=id,name,country_code,place_type,full_name,country,contained_within,geo&poll.fields=id,options,voting_status,end_datetime,duration_minutes";

  @Value("${twitter.tag}")
  private final String twitterTag;
  @Value("${twitter.bearerToken}")
  private final String bearerToken;

  public void streamTwitterFeed() {
    Map<String, String> rules = new HashMap<>();
    rules.put("tag", twitterTag);
    rules.put("maxResults", "2");
    TwitterUtil.setupRules(bearerToken, rules);
    connectStream(bearerToken);
  }

  private void connectStream(String bearerToken) {
    try {
      HttpClient httpClient = HttpClients.custom()
          .setDefaultRequestConfig(RequestConfig.custom()
              .setCookieSpec(CookieSpecs.STANDARD)
              .build())
          .build();
      URIBuilder uriBuilder = new URIBuilder(TWITTER_ENDPOINT);
      HttpGet httpGet = new HttpGet(uriBuilder.build());
      httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
      HttpResponse response = httpClient.execute(httpGet);
      HttpEntity entity = response.getEntity();
      if (null != entity) {
        BufferedReader reader = new BufferedReader(new InputStreamReader((entity.getContent())));
        while (true) {
          String line = reader.readLine();
          if (line != null) {
            log.info("✅ payload received: {}. Sending through kafka", line);
            tweetProcessor.process(line);
          }
        }
      }
    } catch (URISyntaxException | IOException e) {
      log.error("An error occurred", e);
    }
  }

}
