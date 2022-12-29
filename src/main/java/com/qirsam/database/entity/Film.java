package com.qirsam.database.entity;

import java.time.LocalDate;
import java.util.List;

public class Film {
    private Long id;
    private String name;
    private LocalDate dateRelease;
    private Studio studio;
    private List<ActorFilm> actorFilms;

    public Film(Long id, String name, LocalDate dateRelease, Studio studio) {
        this.id = id;
        this.name = name;
        this.dateRelease = dateRelease;
        this.studio = studio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(LocalDate dateRelease) {
        this.dateRelease = dateRelease;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public List<ActorFilm> getActorFilms() {
        return actorFilms;
    }

    public void setActorFilms(List<ActorFilm> actorFilms) {
        this.actorFilms = actorFilms;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateRelease=" + dateRelease +
                ", studio=" + studio +
                '}';
    }
}