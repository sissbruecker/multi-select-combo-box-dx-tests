package com.example.application.data.endpoint;

import com.example.application.data.entity.SoftwareUpdate;
import com.example.application.data.entity.TeslaModel;
import com.example.application.data.service.SoftwareUpdateService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Endpoint
@AnonymousAllowed
public class SoftwareUpdateEndpoint {

    private final SoftwareUpdateService service;

    @Autowired
    public SoftwareUpdateEndpoint(SoftwareUpdateService service) {
        this.service = service;
    }

    @Nonnull
    public Page<@Nonnull SoftwareUpdate> list(Pageable page) {
        return service.list(page);
    }

    public Optional<SoftwareUpdate> get(@Nonnull UUID id) {
        return service.get(id);
    }

    @Nonnull
    public SoftwareUpdate update(@Nonnull SoftwareUpdate entity) {
        return service.update(entity);
    }

    public void delete(@Nonnull UUID id) {
        service.delete(id);
    }

    public int count() {
        return service.count();
    }

}
