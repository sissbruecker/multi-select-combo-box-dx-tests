package com.example.application.data.service;

import com.example.application.data.entity.TeslaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeslaModelRepository extends JpaRepository<TeslaModel, UUID> {

}