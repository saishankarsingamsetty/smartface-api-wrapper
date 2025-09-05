package com.smartface.controller;

import com.smartface.entities.ConfigurationEntity;
import com.smartface.service.ConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configurations")
public class ConfigurationController {

	@Autowired
    private ConfigurationService service;

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
    
    //delete the entity by name
    @PostMapping("/delete/{name}")
    public ResponseEntity<String> deleteById(@PathVariable String name) {
        service.deleteById(name);
        return ResponseEntity.ok("configuration "+name+" deleted successfully");
    }
    
    
}

