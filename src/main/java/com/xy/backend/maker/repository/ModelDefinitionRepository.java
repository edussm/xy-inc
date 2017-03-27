package com.xy.backend.maker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xy.backend.maker.domain.ModelDefinition;

public interface ModelDefinitionRepository extends MongoRepository<ModelDefinition, String> {

}
