package com.xy.backend.maker.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.xy.backend.maker.SpringUnitTest;


public class ModelDefinitionControllerIntegratedTests extends SpringUnitTest {
	private static final String MODEL_DEFINITION_PATH = "modelDefinition";

	MockMvc mockMvc;
	
	@Autowired
	ModelDefinitionController modelDefinitionController;
	
	@Before
	public void init() throws IOException {
		mockMvc = MockMvcBuilders.standaloneSetup(modelDefinitionController)
				.setControllerAdvice(new ExceptionHandlerControllerAdvice())				
				.build();	
	}
	
	// get
	@Test
	public void getAll_MustReturnOk() throws Exception {
		mockMvc.perform(get(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getByModelName_MustReturnOkWitchCorrectData() throws Exception {
		byte[] json = loadFile("/test-data/model-definition-product.json");

		mockMvc.perform(get(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH + "/Produto"))
				.andExpect(content().bytes(json))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getByModelNameInvalidName_MustReturnOkWitchCorrectData() throws Exception {
		mockMvc.perform(get(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH + "/Prod"))
			.andExpect(status().isNotFound());
	}
	
	// insert
	@Test
	public void createWithEmptyData_MustCauseBadRequest() throws Exception {
		byte[] json = loadFile("/test-data/empty.json");
		mockMvc.perform(post(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH)
				.contentType(APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void createWithJsonContainingInvalidType_MustCauseBadRequest() throws Exception {
		byte[] json = loadFile("/test-data/model-definition-product-invalid-type.json");
		mockMvc.perform(post(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH)
				.contentType(APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void createWithAlreadyPupulatedIndex_MustBadRquest() throws Exception {
		byte[] json = loadFile("/test-data/model-definition-product.json");
		mockMvc.perform(post(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH)
				.contentType(APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void createWithValidData_MustCauseCreated() throws Exception {
		byte[] json = loadFile("/test-data/model-definition-user.json");
		mockMvc.perform(post(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH)
				.contentType(APPLICATION_JSON).content(json)).andDo(print()).andExpect(status().isCreated());
		
	}
	
	// update
	
	@Test
	public void updateDataByModelName_MustCauseOk() throws Exception {
		byte[] json = loadFile("/test-data/model-definition-car-update.json");
		mockMvc.perform(put(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH)
				.contentType(APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(content().bytes(json))
				.andExpect(status().isOk());

	}
	
	@Test
	public void updateDataByIdWithInvalidColumnType_MustBadRequest() throws Exception {
		byte[] json = loadFile("/test-data/model-definition-product-invalid-type.json");
		mockMvc.perform(put(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH)
				.contentType(APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());

	}
	
	@Test
	public void updateDataByIdUnavailabe_MustCauseNotFound() throws Exception {
		byte[] json = loadFile("/test-data/model-definition-product-update-id-unavailable.json");
		mockMvc.perform(put(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH)
				.contentType(APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound());
	}
	
	
	// delete
	@Test
	public void deleteDataById_MustCauseOk() throws Exception {
		// Test if Data exists
		byte[] json = loadFile("/test-data/model-definition-motorcycle.json");
		mockMvc.perform(get(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH
				+ "/Moto"))
				.andExpect(content().bytes(json))
				.andExpect(status().isOk());
		
		// Delete
		mockMvc.perform(delete(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH
				+ "/Moto"))
				.andExpect(status().isNoContent());
		
		// Test if Data was removed
		mockMvc.perform(get(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH
				+ "/Moto"))
				.andExpect(status().isNotFound());

	}
	
	@Test
	public void deleteDataByIdUnavailabe_MustCauseNotFound() throws Exception {
		mockMvc.perform(delete(API_ROOT_URL  + "/" + MODEL_DEFINITION_PATH
				+ "/Usuario"))
				.andExpect(status().isNotFound());
	}

}
