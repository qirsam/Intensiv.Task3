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
    id         SERIAL      NOT NULL PRIMARY KEY,
    first_name VARCHAR(64) NOT NUll,
    last_name  VARCHAR(64) NOT NUll,
    birth_date DATE,
    sex        VARCHAR(16)
);
--rollback DROP TABLE actor

--changeset qirsam:2
CREATE TABLE IF NOT EXISTS film
(
    id           SERIAL                                          NOT NULL PRIMARY KEY,
    name         VARCHAR(128)                                    NOT NULL,
    date_release DATE,
    studio_id    BIGINT REFERENCES studio (id) ON DELETE CASCADE NOT NULL

)
