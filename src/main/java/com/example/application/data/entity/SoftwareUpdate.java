package com.example.application.data.entity;


import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class SoftwareUpdate extends AbstractEntity {

    private String version;
    private LocalDate releaseDate;
    @ManyToMany
    private Set<TeslaModel> countries;

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

    public Set<TeslaModel> getCountries() {
        return countries;
    }

    public void setCountries(Set<TeslaModel> countries) {
        this.countries = countries;
    }
}
