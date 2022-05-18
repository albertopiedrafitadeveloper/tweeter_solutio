#EXERCISE

Microservice that persists Tweets into a database to be queried by an API REST. With following restrictions:

- User tweets number have to be greater than (1500 default)
- Language  in list (default es, fr, it)
- Tweet entity must have the following fields:
  - User
  - Text 
  - Localization 
  - Validation
- Api must allow:
  - Query tweets 
  - Mark tweet as validated 
  - Query verified tweets by user.
  - Query N (default 10) tweets most used 
- Use Spring Boot 
- Persist into memory

## As allways twitter is changing its policies -> https://stackoverflow.com/a/72134055 -> So I'm going to avoid using twitter4j since )'ve errors complaining about v2 :( So I've pointed directly towards Twitter API

## Approach
- From the believe in that overengineering is evil, I've used **Spring boot** as required and with **Apache Camel** in order to simplify the development
- I've used **flyway** as a database version control and it's properly configured and Running
- Testing:
  - The application is mainly configuration and POJOS, wich would go outside from sonar Coverage
  - Since I've not been able to use Twitter4j, I'm not going to cover that part
  - I've only coverde by tests the Utility Class JsonNodeUtil 
  
## Structure 
- DataBase feed is done in the new Thread(twitterApiService::streamTwitterFeed).start(); thread
- Rest api definition is located in CamelRoutes class

## Api
- Definition 
  - http://localhost:8080/camel/api-doc
- Get all tweets
  - http://localhost:8080/camel/tweets/all
- Get all tweets verified from user
  - http://localhost:8080/camel/tweets/all/user/EricNunezAP/verified
- Get top used tweets default 10, it can be modified with queryString "quantity"
  - http://localhost:8080/camel/tweets/all/used
  - http://localhost:8080/camel/tweets/all/used?quantity=2
- Put in order to verify a tweet (PUT method)
curl --location --request PUT 'http://localhost:8080/camel/tweets/tweet/1527045486036799488/verified/' \
--header 'Authorization: Bearer ChangeMePlease'

## Considerations 
- Since I have not been able to talk with any functional charge, I have felt free to implement what I understood it was requested.
- No documentation is in code in order to follow CLEAN code rules 
- Since it's an easy development, SOLID principles has little to do with this architecture

## Important 
- Apache kafka have been implemented in order to process the stream queue, but it looks overengineering and that apporach have been abandoned


