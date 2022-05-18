package com.alberto.twitter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tweet_id")
  private String tweetId;
  @Column(name = "twitter_user")
  private String twitterUser;
  private String text;
  private String location;
  private boolean verified;
  @Column(name = "retweet_count")
  private Long retweetCount;

}
