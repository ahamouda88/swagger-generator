package com.groupon.swagger.generator;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.groupon.swagger.common.BaseMavenPluginTest;
import com.swagger.document.generator.DocumentGenerator;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.properties.RefProperty;

public class DocumentGeneratorTest extends BaseMavenPluginTest {

	private Map<String, Path> paths;
	private Swagger swagger;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		final DocumentGenerator documentReader = new DocumentGenerator(mojo.getApiSources().get(0));
		swagger = documentReader.getSwagger();
		paths = swagger.getPaths();
	}

	@Test
	public void testSwaggerPluginComponents() {
		assertEquals("/api", swagger.getBasePath());
		assertEquals(Lists.newArrayList(Scheme.HTTP, Scheme.HTTPS), swagger.getSchemes());
		assertEquals("www.groupon.com", swagger.getHost());
		assertEquals("The description of the API", swagger.getInfo().getDescription());
		assertEquals("http://www.github.com/ahamouda/swagger-generator", swagger.getInfo().getTermsOfService());
		assertEquals("Swagger Maven Plugin Sample", swagger.getInfo().getTitle());
		assertEquals("v1", swagger.getInfo().getVersion());
		// Validate Contact Object
		assertEquals("ahamouda@groupon.com", swagger.getInfo().getContact().getEmail());
		assertEquals("Ahmed Hamouda", swagger.getInfo().getContact().getName());
		assertEquals("http://www.test.com", swagger.getInfo().getContact().getUrl());
		// Validate License Object
		assertEquals("http://www.apache.org/licenses/LICENSE-2.0.html", swagger.getInfo().getLicense().getUrl());
		assertEquals("Apache 2.0", swagger.getInfo().getLicense().getName());
	}
	
	// TODO: Parameters don't work need to find a way to make it work!
	@Test
	public void testSwaggerGetOperation() {
		// Test the GET operation
		Operation getOperation = paths.get("/pet/getPetById").getGet();
		assertEquals("Returns a single pet", getOperation.getDescription());
		assertEquals("Find pet by ID", getOperation.getSummary());
		assertEquals("getPetById", getOperation.getOperationId());
		assertEquals(Lists.newArrayList("pet"), getOperation.getTags());
		assertEquals(Sets.newHashSet("api_key"), getOperation.getSecurity().get(0).keySet());
//		assertEquals(1, getOperation.getParameters().size());
		
		Map<String, Response> getResponses = getOperation.getResponses();
		assertEquals("successful operation", getResponses.get("200").getDescription());
		RefProperty getPetRef = (RefProperty) getResponses.get("200").getSchema();
		assertEquals("#/definitions/Pet", getPetRef.get$ref());
		assertEquals("Pet", getPetRef.getSimpleRef());
		assertEquals("Invalid ID supplied", getResponses.get("400").getDescription());
		assertEquals("Pet not found", getResponses.get("404").getDescription());
	}
	
	@Test
	public void testSwaggerDeleteOperation() {
		// Test the DELETE operation
		Operation deleteOperation = paths.get("/pet/deletePet").getDelete();
		assertEquals("Deletes a pet", deleteOperation.getSummary());
		assertEquals("deletePet", deleteOperation.getOperationId());
		assertEquals(Lists.newArrayList("pet"), deleteOperation.getTags());
		assertEquals(Sets.newHashSet("petstore_auth"), deleteOperation.getSecurity().get(0).keySet());
		assertEquals(Lists.newArrayList("write:pets", "read:pets"), deleteOperation.getSecurity().get(0).get("petstore_auth"));
//		assertEquals(1, getOperation.getParameters().size());
		
		Map<String, Response> deleteResponses = deleteOperation.getResponses();
		assertEquals("Invalid ID supplied", deleteResponses.get("400").getDescription());
		assertEquals("Pet not found", deleteResponses.get("404").getDescription());
	}
	
	@Test
	public void testSwaggerPostOperation() {
		// Test the POST operation
		Operation postOperation = paths.get("/pet/uploadFile").getPost();
		assertEquals("uploads an image", postOperation.getSummary());
		assertEquals("uploadFile", postOperation.getOperationId());
		assertEquals(Lists.newArrayList("pet"), postOperation.getTags());
		assertEquals(Sets.newHashSet("petstore_auth"), postOperation.getSecurity().get(0).keySet());
		assertEquals(Lists.newArrayList("write:pets", "read:pets"), postOperation.getSecurity().get(0).get("petstore_auth"));
//		assertEquals(1, getOperation.getParameters().size());

		Map<String, Response> postResponses = postOperation.getResponses();
		assertEquals("successful operation", postResponses.get("200").getDescription());
		RefProperty postPetRef = (RefProperty) postResponses.get("200").getSchema();
		assertEquals("#/definitions/Pet", postPetRef.get$ref());
		assertEquals("Pet", postPetRef.getSimpleRef());
	}

	@Test
	public void testSwaggerPutOperation() {	    
		// Test the PUT operation
		Operation putOperation = paths.get("/pet/updatePet").getPut();
		assertEquals("Update an existing pet", putOperation.getSummary());
		assertEquals("updatePet", putOperation.getOperationId());
		assertEquals(Lists.newArrayList("pet"), putOperation.getTags());
		assertEquals(Sets.newHashSet("petstore_auth"), putOperation.getSecurity().get(0).keySet());
		assertEquals(Lists.newArrayList("write:pets", "read:pets"), putOperation.getSecurity().get(0).get("petstore_auth"));
//		assertEquals(1, getOperation.getParameters().size());

		Map<String, Response> putResponses = putOperation.getResponses();
		assertEquals("Invalid ID supplied", putResponses.get("400").getDescription());
		assertEquals("Pet not found", putResponses.get("404").getDescription());
		assertEquals("Validation exception", putResponses.get("405").getDescription());
	}
	
}
