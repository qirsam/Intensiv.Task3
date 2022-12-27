--liquibase formatted sql

--changeset qirsam:1
CREATE TABLE IF NOT EXISTS studio
(
    id                 SERIAL       NOT NULL PRIMARY KEY,
    name               VARCHAR(128) NOT NULL,
    date_of_foundation DATE
);
--rollback DROP TABLE studio;

--changeset qirsam:2
CREATE TABLE IF NOT EXISTS actor
(
    id        SERIAL      NOT NULL PRIMARY KEY,
    firstname VARCHAR(64) NOT NUll,
    lastname  VARCHAR(64) NOT NUll,
    birthdate DATE,
    sex       VARCHAR(16)
);
--rollback DROP TABLE actor

--changeset qirsam:2
CREATE TABLE IF NOT EXISTS film
(
    id           SERIAL                                          NOT NULL PRIMARY KEY,
    name         VARCHAR(128)                                    NOT NULL,
    date_release DATE,
    studio_id    BIGINT REFERENCES studio (id) ON DELETE CASCADE NOT NULL
);

--changeset qirsam:3
CREATE TABLE IF NOT EXISTS actor_film
(
    id       SERIAL NOT NULL PRIMARY KEY,
    actor_id BIGINT NOT NULL REFERENCES actor (id) ON DELETE CASCADE,
    film_id  BIGINT NOT NULL REFERENCES film (id) ON DELETE CASCADE
);

INSERT INTO studio (name, date_of_foundation)
VALUES ('Paramount Pictures', '1912-05-01'),
       ('Walt Disney Studios', '1923-10-16'),
       ('Warner Bros', '1923-01-01'),
       ('20th Century Studios', '1935-05-31'),
       ('Sony Pictures', '1991-08-07'),
       ('Columbia Pictures', '1918-06-19'),
       ('Universal Pictures', '1912-04-30');

INSERT INTO actor(firstname, lastname, birthdate, sex)
VALUES ('Tom', 'Hardy', '1977-09-15', 'male'),
       ('Jonny', 'Depp', '1963-06-09', 'male'),
       ('Keanu', 'Reeves', '1964-09-08', 'male'),
       ('Brad', 'Pitt', '1963-12-18', 'male'),
       ('Matt', 'Daemon', '1970-10-08', 'male'),
       ('Tom', 'Cruise', '1962-07-03', 'male'),
       ('Robert', 'Downey Jr', '1965-04-04', 'male');

