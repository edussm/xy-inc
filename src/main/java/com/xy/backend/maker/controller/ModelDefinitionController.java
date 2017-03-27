package com.xy.backend.maker.controller;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xy.backend.maker.domain.ModelDefinition;
import com.xy.backend.maker.service.ModelDefinitionService;

@Controller
@RequestMapping(value="/api/v1/modelDefinition")
public class ModelDefinitionController {
	
	private final ModelDefinitionService modelDefinitionService;
	
	@Autowired
	public ModelDefinitionController(ModelDefinitionService modelDefinitionService) {
		this.modelDefinitionService = modelDefinitionService;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Collection<ModelDefinition>> getAllModels() {
		
		return new ResponseEntity<>(modelDefinitionService.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value="/{modelName}", method=RequestMethod.GET)
	public ResponseEntity<ModelDefinition> getModelByName(@PathVariable String modelName) {
		
		return new ResponseEntity<>(modelDefinitionService.findModelDefinitionByName(modelName)
				.orElseThrow(() -> new NoSuchElementException()), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<ModelDefinition> createModelDefinition(@RequestBody ModelDefinition modelDefinition) {
		
		return new ResponseEntity<>(modelDefinitionService.create(modelDefinition)
				.orElseThrow(() -> new IllegalArgumentException()), HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.PUT, produces="application/json", consumes="application/json")
	public ResponseEntity<ModelDefinition> updateModelDefinition(@RequestBody ModelDefinition modelDefinition) {
		
		return new ResponseEntity<>(modelDefinitionService.update(modelDefinition)
				.orElseThrow(() -> new IllegalArgumentException()), HttpStatus.OK);
	}
	
	@RequestMapping(value="/{modelName}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteModelDefinition(@PathVariable String modelName) {
		
		modelDefinitionService.deleteModelByName(modelName);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
