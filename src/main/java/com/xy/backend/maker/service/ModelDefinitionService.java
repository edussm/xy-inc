package com.xy.backend.maker.service;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xy.backend.maker.domain.ModelDefinition;
import com.xy.backend.maker.repository.ModelDefinitionRepository;

@Service
public class ModelDefinitionService {

	private static final Logger log = LoggerFactory.getLogger(ModelDefinitionService.class);
	
	private final ModelDefinitionRepository modelDefinitionRepository;

	@Autowired
	public ModelDefinitionService(ModelDefinitionRepository modelDefinitionRepository) {
		this.modelDefinitionRepository = modelDefinitionRepository;
	}
	
	public Collection<ModelDefinition> findAll() {
		log.debug("Buscando Todas as Definicoes de Modelos");
		
		Collection<ModelDefinition> modelDefinitions = modelDefinitionRepository.findAll();
		log.debug("Registros encontrados={}", modelDefinitions);

		return modelDefinitions;
	}
	
	public Optional<ModelDefinition> findModelDefinitionByName(String modelName) {
		log.debug("Buscando Definicao de Dados com id={}", modelName);
		
		ModelDefinition modelDefinition = modelDefinitionRepository.findOne(modelName);
		log.debug("Registro encontrado={}", modelDefinition);

		if (modelDefinition == null) {
			return Optional.empty();
		} 

		return Optional.of(modelDefinition);
	}
	
	public void deleteModelByName(String modelName) {
		log.debug("Apagando Definicao de Dados com id={}", modelName);
		
		ModelDefinition modelDefinition = modelDefinitionRepository.findOne(modelName);
		if (modelDefinition == null) {		
			log.debug("Registro a ser apagado nao encontrado. Id={}", modelName);
			throw new NoSuchElementException();
		}
		
		modelDefinitionRepository.delete(modelName);
		log.debug("Registro apagado={}", modelName);
	}
	
	public Optional<ModelDefinition> create(ModelDefinition modelDefinition) {
		log.debug("Criando Definicao de Dados do tipo {}", modelDefinition);

		if (validateData(modelDefinition.getModelName(), modelDefinition.getColumns())) {
			modelDefinition = modelDefinitionRepository.save(modelDefinition);
			log.debug("Registro criado={}", modelDefinition);
			
			return Optional.of(modelDefinition);
		}
		
		return Optional.empty();
	}
	
	public Optional<ModelDefinition> update(ModelDefinition modelDefinition) {
		log.debug("Atualizando Definicao de Dados do tipo {}", modelDefinition);
		
		if (modelDefinitionRepository.findOne(modelDefinition.getModelName()) == null) {		
			log.debug("Registro a ser atualizado nao encontrado. ModelName={}", modelDefinition.getModelName());
			throw new NoSuchElementException();
		}

		if (validateColumns(modelDefinition.getColumns())) {
			modelDefinitionRepository.save(modelDefinition);
			log.debug("Registro atualizado={}", modelDefinition);
			
			return Optional.of(modelDefinition);
		}
		
		return Optional.empty();
	}
	
	private boolean validateData(String modelName, Map<String, String> columns) {
		log.debug("Validando Definicao de Modelo de dados. modelName={}. Columns={}", modelName, columns);
		
		ModelDefinition modelDefinition = modelDefinitionRepository.findOne(modelName);

		
		if (modelDefinition == null && !columns.isEmpty()) {
			for (Map.Entry<String, String> entry : columns.entrySet()) {
				if (!checkType(entry.getValue())) {
					log.debug("Dados invalidos (tipo de dados invalidos). Coluna={}, Tipo={}", 
							entry.getKey(), entry.getValue());
					return false;
				} 
			}
		} else {
			log.debug("Dados invalidos! (Model definition = {}, Columns={}", modelDefinition, columns);
			return false;
		}

		log.debug("VALIDADO = ModelDefinition modelName={}. Columns={}", modelName, columns);
		return true;
	}
	
	private boolean validateColumns(Map<String, String> columns) {
		log.debug("Validando Columns={}", columns);
		
		if (!columns.isEmpty()) {
			for (Map.Entry<String, String> entry : columns.entrySet()) {
				if (!checkType(entry.getValue())) {
					log.debug("Dados invalidos (tipo de dados invalidos). Coluna={}, Tipo={}", 
							entry.getKey(), entry.getValue());
					return false;
				} 
			}
		} else {
			log.debug("Dados invalidos! Columns={}", columns);
			return false;
		}

		log.debug("VALIDADO = Columns={}", columns);
		return true;
	}

	private boolean checkType(String type) {
		log.debug("Checando a validade do tipo={}", type);
		switch (type.toLowerCase()) {
			case "string":
			case "text":
			case "int":
			case "decimal":
			case "double":
				return true;
		}
		return false;
	}
	
}
