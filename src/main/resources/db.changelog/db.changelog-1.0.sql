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
       ('Robert', 'Downey Jr', '1965-04-04', 'male'),
       ('Orlando', 'Bloom', '1977-01-13', 'male'),
       ('Keira', 'Knightley', '1985-03-26', 'female'),
       ('Ryan', 'Gosling', '1980-11-12', 'male'),
       ('Ana', 'de Armas', '1988-04-30', 'female'),
       ('Edward', 'Norton', '1969-08-18', 'male'),
       ('Jared', 'Leto', '1971-12-26', 'male'),
       ('Helena', 'Bonham Carte', '1966-05-26', 'female'),
       ('Jason', 'Statham', '1967-07-26', 'male'),
       ('Christoph', 'Waltz', '1956-10-04', 'male'),
       ('Leonardo', 'DiCaprio', '1974-11-11', 'male'),
       ('Margot', 'Robbie', '1990-07-02', 'female');

INSERT INTO film(name, date_release, studio_id)
VALUES ('Pirates of the Caribbean', '2003-06-28', '2'),
       ('The Wolf of Wall Street,', '2013-12-09', '1'),
       ('Blade Runner 2049', '2017-10-03', '3'),
       ('Fight Club', '1999-09-10', '4'),
       ('Once Upon a Time in... Hollywood', '2019-05-21', '5'),
       ('Snatch', '2000-08-23', '6'),
       ('Inglourious Basterds', '2009-05-20', '7');

INSERT INTO actor_film(actor_id, film_id)
VALUES ('2', '1'),
       ('8', '1'),
       ('9', '1'),
       ('17', '2'),
       ('18', '2'),
       ('11', '3'),
       ('10', '3'),
       ('13', '3'),
       ('14', '4'),
       ('4', '4'),
       ('14', '4'),
       ('12', '4'),
       ('4', '5'),
       ('17', '5'),
       ('18', '5'),
       ('4', '6'),
       ('15', '6'),
       ('4', '7'),
       ('16', '7');



