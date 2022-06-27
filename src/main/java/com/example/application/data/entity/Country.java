package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Country extends AbstractEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
