package com.xy.backend.maker.controller;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xy.backend.maker.domain.ModelData;
import com.xy.backend.maker.service.ModelDataService;

@Controller
@RequestMapping(value="/api/dynamic")
public class ModelDataController {
	
	private final ModelDataService modelDataService;
	
	@Autowired
	public ModelDataController(ModelDataService modelDataService) {
		this.modelDataService = modelDataService;
	}

	@RequestMapping(value="/{modelName}/{id}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getModelById(@PathVariable String modelName, @PathVariable String id) {
		
		return new ResponseEntity<>(modelDataService.findModelById(modelName, id)
				.orElseThrow(() -> new NoSuchElementException()), HttpStatus.OK);
	}
	
	@RequestMapping(value="/{modelType}", method=RequestMethod.GET)
	public ResponseEntity<Collection<ModelData>> getModel(@PathVariable String modelType) {
		
		return new ResponseEntity<>(modelDataService.getModelDataByModelName(modelType)
				.orElseThrow(() -> new NoSuchElementException()), HttpStatus.OK);
	}
	
	@RequestMapping(value="/{modelName}", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<Map<String, Object>> createModel(@PathVariable String modelName, @RequestBody Map<String, Object> data) {
		
		return new ResponseEntity<>(modelDataService.create(modelName, data)
				.orElseThrow(() -> new IllegalArgumentException()), HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{modelName}/{id}", method=RequestMethod.PUT, produces="application/json", consumes="application/json")
	public ResponseEntity<Map<String, Object>> updateModel(@PathVariable String modelName, @PathVariable String id, 
			@RequestBody Map<String, Object> data) {
		
		return new ResponseEntity<>(modelDataService.update(modelName, id, data)
				.orElseThrow(() -> new IllegalArgumentException()), HttpStatus.OK);
	}
	
	@RequestMapping(value="/{modelName}/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteModel(@PathVariable String modelName, @PathVariable String id) {
		
		modelDataService.deleteModelById(modelName, id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
