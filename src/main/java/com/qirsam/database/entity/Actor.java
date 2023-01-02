package com.qirsam.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Actor {

    private Long id;

    private String firstname;

    private String lastname;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    private String sex;

    private List<Film> films;

    public Actor() {
    }

    public Actor(Long id, String firstname, String lastname, LocalDate birthDate, String sex) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.films = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(id, actor.id) && Objects.equals(firstname, actor.firstname) && Objects.equals(birthDate, actor.birthDate) && Objects.equals(sex, actor.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, birthDate, sex);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthDate=" + birthDate +
                ", sex='" + sex + '\'' +
                '}';
    }
}
