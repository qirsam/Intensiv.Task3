package com.qirsam.database.entity;

public class ActorFilm {
    private Long id;
    private Actor actor;
    private Film film;

    public ActorFilm(Long id, Actor actor, Film film) {
        this.id = id;
        this.actor = actor;
        this.actor.getActorFilms().add(this);
        this.film = film;
        this.film.getActorFilms().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        this.actor.getActorFilms().add(this);
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
        this.film.getActorFilms().add(this);
    }
}
