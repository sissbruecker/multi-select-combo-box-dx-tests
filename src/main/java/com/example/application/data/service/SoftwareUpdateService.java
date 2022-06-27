package com.example.application.data.service;

import com.example.application.data.entity.SoftwareUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SoftwareUpdateService {

    private final SoftwareUpdateRepository repository;

    @Autowired
    public SoftwareUpdateService(SoftwareUpdateRepository repository) {
        this.repository = repository;
    }

    public Optional<SoftwareUpdate> get(UUID id) {
        return repository.findById(id);
    }

    public SoftwareUpdate update(SoftwareUpdate entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<SoftwareUpdate> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
