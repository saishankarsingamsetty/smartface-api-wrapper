package com.smartface.service;



import com.smartface.entities.ConfigurationEntity;
import com.smartface.repository.ConfigurationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {

	@Autowired
    private ConfigurationRepository repository;

    public ConfigurationEntity save(ConfigurationEntity config) {
        return repository.save(config); // works for insert + update
    }

    public List<ConfigurationEntity> getAll() {
        return repository.findAll();
    }

    public ConfigurationEntity getById(String name) {
        return repository.findById(name).orElse(null);
    }
    
    public void deleteById(String name) {
    	 repository.deleteById(name);
    }
}

