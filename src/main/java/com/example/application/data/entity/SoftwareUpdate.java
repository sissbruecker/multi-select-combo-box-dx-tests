package com.example.application.data.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class SoftwareUpdate extends AbstractEntity {

    private String version;
    private LocalDate releaseDate;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<TeslaModel> models;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Set<TeslaModel> getModels() {
        return models;
    }

    public void setModels(Set<TeslaModel> models) {
        this.models = models;
    }
}
