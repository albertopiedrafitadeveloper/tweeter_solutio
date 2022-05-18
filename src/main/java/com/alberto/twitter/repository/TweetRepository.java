package com.alberto.twitter.repository;

import com.alberto.twitter.domain.Tweet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<Tweet, Integer> {

  Optional<Tweet> findByTweetId(String tweetId);

  List<Tweet> findAllByTwitterUserAndVerifiedTrue(String twitterUser);

  @Query(value = "select * from tweet t order by retweet_count DESC limit :limit", nativeQuery = true)
  List<Tweet> findMostUsed(int limit);

}
