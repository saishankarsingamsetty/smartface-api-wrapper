package com.smartface.controller;

import com.smartface.entities.ConfigurationEntity;
import com.smartface.service.ConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configurations")
public class ConfigurationController {

    private final ConfigurationService service;

    public ConfigurationController(ConfigurationService service) {
        this.service = service;
    }

    // Insert or Update
    @PostMapping("/save")
    public ResponseEntity<ConfigurationEntity> save(@RequestBody ConfigurationEntity config) {
        return ResponseEntity.ok(service.save(config));
    }

    // Get All
    @GetMapping("/all")
    public ResponseEntity<List<ConfigurationEntity>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // Get by name (primary key)
    @GetMapping("/{name}")
    public ResponseEntity<ConfigurationEntity> getById(@PathVariable String name) {
        ConfigurationEntity entity = service.getById(name);
        if (entity != null) {
            return ResponseEntity.ok(entity);
        }
        return ResponseEntity.notFound().build();
    }
}

