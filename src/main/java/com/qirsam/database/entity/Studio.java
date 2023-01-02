package com.qirsam.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Studio {

    private Long id;

    private String name;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfFoundation;

    public Studio(Long id, String name, LocalDate dateOfFoundation) {
        this.id = id;
        this.name = name;
        this.dateOfFoundation = dateOfFoundation;
    }

    public Studio() {
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

    public LocalDate getDateOfFoundation() {
        return dateOfFoundation;
    }

    public void setDateOfFoundation(LocalDate dateOfFoundation) {
        this.dateOfFoundation = dateOfFoundation;
    }

    @Override
    public String toString() {
        return "Studio{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfFoundation=" + dateOfFoundation +
                '}';
    }
}
