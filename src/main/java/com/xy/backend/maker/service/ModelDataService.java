package com.xy.backend.maker.service;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xy.backend.maker.domain.ModelData;
import com.xy.backend.maker.domain.ModelDefinition;
import com.xy.backend.maker.repository.ModelDataRepository;
import com.xy.backend.maker.repository.ModelDefinitionRepository;

@Service
public class ModelDataService {

	private static final Logger log = LoggerFactory.getLogger(ModelDataService.class);
	
	private final ModelDataRepository modelDataRepository;
	private final ModelDefinitionRepository modelDefinitionRepository;

	@Autowired
	public ModelDataService(ModelDataRepository modelDataRepository, ModelDefinitionRepository modelDefinitionRepository) {
		this.modelDataRepository = modelDataRepository;
		this.modelDefinitionRepository = modelDefinitionRepository;
	}
	
	public Optional<Map<String, Object>> findModelById(String modelName, String id) {
		log.debug("Buscando Modelo={} com id={}", modelName, id);
		
		UUID uuid;
		
		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {
			log.debug("UUID inválido={}", id);
			return Optional.empty();
		}
		
		ModelData modelData = modelDataRepository.findByIdAndModelName(uuid, modelName);
		
		if (modelData == null) {
			return Optional.empty();
		} 
		
		Map<String, Object> data = modelData.getData();
		log.debug("Registro encontrado={}", modelData);

		data.put("id", modelData.getId());

		return Optional.of(data);
	}
	
	public Optional<Collection<ModelData>> getModelDataByModelName(String modelName) {
		log.debug("Buscando os Dados do tipo={}", modelName);
		
		Collection<ModelData> result = modelDataRepository.findByModelName(modelName);
		
		log.debug("Encontrado {} documentos", result == null ? 0 : result.size());
		
		return (result == null || result.isEmpty()) ? Optional.empty() : Optional.of(result);
	}
	
	public void deleteModelById(String modelName, String id) {
		log.debug("Apagando Modelo={} com id={}", modelName, id);
		
		UUID uuid;
		
		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {
			log.debug("UUID inválido={}", id);
			throw new NoSuchElementException();
		}
		
		ModelData modelData = modelDataRepository.findByIdAndModelName(uuid, modelName);
		
		if (modelData == null) {		
			log.debug("Registro a ser apagado nao encontrado. Id={}", uuid);
			throw new NoSuchElementException();
		}
		
		modelDataRepository.delete(modelData);
		log.debug("Registro apagado={}", modelData);
	}
	
	public Optional<Map<String, Object>> create(String modelName, Map<String, Object> data) {
		log.debug("Criando um registro do tipo {}", modelName);

		if (validateData(modelName, data)) {
			ModelData modelData = modelDataRepository.save(new ModelData(UUID.randomUUID(), modelName, data));
			log.debug("Registro criado={}", modelData);
			modelData.getData().put("id", modelData.getId());
			return Optional.of(modelData.getData());
		}
		
		return Optional.empty();
	}
	
	public Optional<Map<String, Object>> update(String modelName, String id, Map<String, Object> data) {
		log.debug("Atualizando um registro do tipo={}, id={}", modelName, id);
		
		data.remove("id");

		UUID uuid;
		
		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {
			log.debug("UUID inválido={}", id);
			return Optional.empty();
		}
		
		ModelData modelData = modelDataRepository.findByIdAndModelName(uuid, modelName);

		if (validateData(modelName, data) && modelData != null) {
			log.debug("Registro validado e existente. Dados atuais={}. Novos dados={}", 
					modelData, data);
			
			modelData = modelDataRepository.save(new ModelData(uuid, modelName, data));
			log.debug("Registro atualizado={}", modelData);

			modelData.getData().put("id", modelData.getId());
			
			return Optional.of(modelData.getData());
		}
		
		return Optional.empty();
	}
	
	private boolean validateData(String modelName, Map<String, Object> data) {
		ModelDefinition modelDefinition = modelDefinitionRepository.findOne(modelName);
		log.debug("Validando Modelo modelName={}. Data={}, Model Definition={}", modelName, data, modelDefinition);
		
		if (modelDefinition != null && !data.isEmpty()) {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				if (!modelDefinition.getColumns().containsKey(entry.getKey()) ||
						!checkType(modelDefinition.getColumns().get(entry.getKey()), entry.getValue())) {
					log.debug("Dados invalidos (coluna e/ou tipo de dados invalidos). Coluna={}", entry.getKey());
					return false;
				} 
			}
		} else {
			log.debug("Dados invalidos (Model definition nao encontrado para modelName={}", modelName);
			return false;
		}

		log.debug("VALIDADO = Modelo modelName={}. Data={}, Model Definition={}", modelName, data, modelDefinition);
		return true;
	}

	private boolean checkType(String type, Object object) {
		log.debug("Checando o tipo={} para o objeto={}, classe={}", type, object, object.getClass());
		switch (type.toLowerCase()) {
			case "string":
			case "text":
				return object instanceof String;
			case "int":
			case "decimal":
			case "double":
				return object instanceof Double || object instanceof Integer;
		}
		return false;
	}

	
}
