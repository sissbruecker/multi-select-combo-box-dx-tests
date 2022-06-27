package com.example.application.data.service;

import com.example.application.data.entity.SoftwareUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SoftwareUpdateRepository extends JpaRepository<SoftwareUpdate, UUID> {

}