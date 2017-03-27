package com.xy.backend.maker.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.xy.backend.maker.SpringUnitTest;


public class ModelDataControllerIntegratedTests extends SpringUnitTest {
	private static final String PRODUCT_MODEL_NAME = "Produto";
	
	MockMvc mockMvc;
	
	@Autowired
	ModelDataController modelDataController;
	
	@Before
	public void init() throws IOException {
		mockMvc = MockMvcBuilders.standaloneSetup(modelDataController)
				.setControllerAdvice(new ExceptionHandlerControllerAdvice())				
				.build();	
	}
	
	@Test
	public void createWithEmptyData_MustCauseBadRequest() throws Exception {
		byte[] json = loadFile("/test-data/empty.json");
		mockMvc.perform(post(MAKER_API_ROOT_URL + "/" + PRODUCT_MODEL_NAME)
				.contentType(APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void createWithJsonContainingInvalidField_MustCauseBadRequest() throws Exception {
		byte[] json = loadFile("/test-data/model-product-invalid-field-1.json");
		mockMvc.perform(post(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME)
				.contentType(APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void createWithJsonContainingInvalidDataType_MustCauseBadRequest() throws Exception {
		byte[] json = loadFile("/test-data/model-product-invalid-data-type-1.json");
		mockMvc.perform(post(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME)
				.contentType(APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void createWithValidData_MustCauseCreated() throws Exception {
		byte[] json = loadFile("/test-data/model-product-valid-1.json");
		mockMvc.perform(post(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME)
				.contentType(APPLICATION_JSON).content(json)).andExpect(status().isCreated());
		
	}
	
	@Test
	public void createWithValidDataWithInvalidModelName_MustCauseBadRequest() throws Exception {
		byte[] json = loadFile("/test-data/model-product-valid-1.json");
		mockMvc.perform(post(MAKER_API_ROOT_URL  + "/Prod")
				.contentType(APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void getAllDataWithInvalidModelName_MustCauseNotFound() throws Exception {
		mockMvc.perform(get(MAKER_API_ROOT_URL  + "/Prod"))
				.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void getAllDataWithValidModelName_MustCauseOk() throws Exception {
		mockMvc.perform(get(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getDataByIdWithInalidId_MustCauseOk() throws Exception {
		mockMvc.perform(get(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME 
				+ "/fa16cdb9-b61c-4778-9979-62853a06feb3"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getDataByIdUnavailabe_MustCauseNotFound() throws Exception {
		mockMvc.perform(get(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME 
				+ "/fa16cdb9-b61c-4778-9979-62853a06feb4"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateDataById_MustCauseOk() throws Exception {
		byte[] json = loadFile("/test-data/model-product-valid-2-update.json");
		byte[] jsonResponse = loadFile("/test-data/model-product-valid-2-update-response.json");
		mockMvc.perform(put(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME
				+ "/fa16cdb9-b61c-4778-9979-62853a06feb3")
				.contentType(APPLICATION_JSON).content(json))
				.andExpect(content().bytes(jsonResponse))
				.andExpect(status().isOk());

	}
	
	@Test
	public void updateDataByIdWithInvalidColumn_MustBadRequest() throws Exception {
		byte[] json = loadFile("/test-data/model-product-invalid-2-update.json");
		mockMvc.perform(put(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME
				+ "/fa16cdb9-b61c-4778-9979-62853a06feb3")
				.contentType(APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());

	}
	
	@Test
	public void updateDataByIdUnavailabe_MustCauseNotFound() throws Exception {
		byte[] json = loadFile("/test-data/model-product-valid-2-update.json");
		mockMvc.perform(get(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME 
				+ "/fa16cdb9-b61c-4778-9979-62853a06feb4")
				.contentType(APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound());
	}
	
	// delete

	@Test
	public void deleteDataById_MustCauseOk() throws Exception {
		// Test if Data exists
		byte[] json = loadFile("/test-data/model-product-valid-3-delete.json");
		mockMvc.perform(get(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME 
				+ "/2a88bec3-1923-4ef7-97f1-2b8da0632f1a"))
				.andExpect(content().bytes(json))
				.andExpect(status().isOk());
		
		// Delete
		mockMvc.perform(delete(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME
				+ "/2a88bec3-1923-4ef7-97f1-2b8da0632f1a"))
				.andExpect(status().isNoContent());
		
		// Test if Data was removed
		mockMvc.perform(get(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME 
				+ "/2a88bec3-1923-4ef7-97f1-2b8da0632f1a"))
				.andExpect(status().isNotFound());

	}
	
	@Test
	public void deleteDataByIdUnavailabe_MustCauseNotFound() throws Exception {
		mockMvc.perform(delete(MAKER_API_ROOT_URL  + "/" + PRODUCT_MODEL_NAME 
				+ "/fa16cdb9-b61c-4778-9979-62853a06feb4"))
				.andExpect(status().isNotFound());
	}
}
