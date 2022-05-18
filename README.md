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
  - Query tweets by user.
  - Query N (default 10) tweets most used 
- Use Spring Boot 
- Persist into memory

## As allways twitter is amazing -> https://stackoverflow.com/a/72134055 -> So I'm going to avoid using twitter4j :(


http://localhost:8080/camel/tweets/