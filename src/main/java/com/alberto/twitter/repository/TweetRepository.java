package com.alberto.twitter.repository;

import com.alberto.twitter.domain.Tweet;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<Tweet, Integer> {

  Optional<Tweet> findByTweetId(String tweetId);

}
