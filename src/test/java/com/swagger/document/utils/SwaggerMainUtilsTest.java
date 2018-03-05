package com.swagger.document.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.swagger.document.utils.SwaggerMainUtils;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

public class SwaggerMainUtilsTest {

	@Test
	public void testGetInfoFromAnnotation() {
		io.swagger.models.Info info = SwaggerMainUtils.getInfoFromAnnotation();
		assertEquals("description", info.getDescription());
		assertEquals("v1", info.getVersion());
		assertEquals("My API", info.getTitle());
		assertEquals("http://example.com", info.getTermsOfService());
		assertEquals("Ahmed", info.getContact().getName());
		assertEquals("ahmed@groupon.com", info.getContact().getEmail());
		assertEquals("http://mywebsite.com", info.getContact().getUrl());
		assertEquals("license", info.getLicense().getName());
		assertEquals("http://license.com/", info.getLicense().getUrl());
	}

	@Test
	public void testGetBasePathAndHostFromAnnotation() {
		String basePath = SwaggerMainUtils.getBasePathFromAnnotation();
		assertEquals("/api-docs", basePath);

		String host = SwaggerMainUtils.getHostFromAnnotation();
		assertEquals("www.google.com", host);
	}

	@SwaggerDefinition(basePath = "/api-docs", host = "www.google.com", info = @Info(description = "description", version = "v1", title = "My API", termsOfService = "http://example.com"))
	private class TestSwaggerDefinitionPart1 {
	}

	@SwaggerDefinition(basePath = "/api-docs", host = "www.google.com", info = @Info(contact = @Contact(name = "Ahmed", email = "ahmed@groupon.com", url = "http://mywebsite.com"), license = @License(name = "license", url = "http://license.com/"), version = "v1", title = "My API"))
	private class TestSwaggerDefinitionPart2 {
	}
}
