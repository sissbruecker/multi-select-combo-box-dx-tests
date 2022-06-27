package com.example.application.data.endpoint;

import com.example.application.data.entity.TeslaModel;
import com.example.application.data.service.TeslaModelService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
@AnonymousAllowed
public class TeslaModelEndpoint {

    private final TeslaModelService service;

    @Autowired
    public TeslaModelEndpoint(TeslaModelService service) {
        this.service = service;
    }

    @Nonnull
    public List<@Nonnull TeslaModel> listAll() {
        return service.listAll();
    }
}
