package com.xy.backend.maker.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xy.backend.maker.domain.ModelData;

public interface ModelDataRepository extends MongoRepository<ModelData, UUID> {
	Collection<ModelData> findByModelName(String modelName);
	ModelData findByIdAndModelName(UUID id, String modelName);
}
