create table tweet
(
    id              bigserial
                    constraint tweet_pk
                    primary key,
    tweet_id        varchar(255),
    twitter_user    varchar(255),
    text            text,
    latitude        varchar(255),
    longitude       varchar(255),
    validated       bit,
    retweet_count   bigint
);

create unique index tweet_id_uindex
    on tweet (id);

create index tweet_user_index
    on tweet (twitter_user);

