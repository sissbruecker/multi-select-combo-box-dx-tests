package com.example.application.data.service;

import com.example.application.data.entity.TeslaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeslaModelService {

    private final TeslaModelRepository repository;

    @Autowired
    public TeslaModelService(TeslaModelRepository repository) {
        this.repository = repository;
    }

    public Optional<TeslaModel> get(UUID id) {
        return repository.findById(id);
    }

    public TeslaModel update(TeslaModel entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<TeslaModel> listAll() {
        return repository.findAll();
    }

    public int count() {
        return (int) repository.count();
    }

}
