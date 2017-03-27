package com.xy.backend.maker;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xy.backend.maker.repository.ModelDataRepository;
import com.xy.backend.maker.repository.ModelDefinitionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public abstract class SpringUnitTest {
	protected final static String API_ROOT_URL="/api/v1";
	protected final static String MAKER_API_ROOT_URL="/api/dynamic";
	
	@Autowired
	protected ModelDataRepository modelDataRepository;

	@Autowired
	protected ModelDefinitionRepository modelDefinitionRepository;

	public static byte[] loadFile(String location) throws Exception {
		URL resourceUrl = SpringUnitTest.class.getResource(location);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return Files.readAllBytes(resourcePath);
	}
}
