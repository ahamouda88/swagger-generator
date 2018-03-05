package com.swagger.document.mavenplugin;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.swagger.document.mavenplugin.ApiDocumentMojo;
import com.swagger.document.mavenplugin.ApiSource;

import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;

public class ApiDocumentMojoTest extends AbstractMojoTestCase {
	private static final String PLUGIN_CONFIG_PATH = "src/test/resources/plugin/plugin-config.xml";

	private ApiDocumentMojo mojo;
	private List<SwaggerExtension> extensions;

	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();

		File testPom = new File(getBasedir(), PLUGIN_CONFIG_PATH);
		mojo = (ApiDocumentMojo) lookupMojo("generate", testPom);
	}

	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		SwaggerExtensions.setExtensions(extensions);
	}

	@Test
	public void testApiSourceParameters() throws Exception {
		List<ApiSource> apiSources = mojo.getApiSources();
		assertEquals(1, apiSources.size());

		ApiSource apiSource = apiSources.get(0);
		assertEquals("/api", apiSource.getBasePath());
		assertEquals("www.groupon.com", apiSource.getHost());
		assertEquals("com.groupon.springmvc", apiSource.getLocations().get(0));
		assertEquals(Lists.newArrayList("http", "https"), apiSource.getSchemes());
		assertEquals("Swagger Maven Plugin Sample", apiSource.getInfo().getTitle());
		assertEquals("v1", apiSource.getInfo().getVersion());
		assertEquals("The description of the API", apiSource.getInfo().getDescription());
		assertEquals("http://www.github.com/ahamouda/swagger-generator", apiSource.getInfo().getTermsOfService());
		// Validate Contact Object
		assertEquals("ahamouda@groupon.com", apiSource.getInfo().getContact().getEmail());
		assertEquals("Ahmed Hamouda", apiSource.getInfo().getContact().getName());
		assertEquals("http://www.test.com", apiSource.getInfo().getContact().getUrl());
		// Validate License Object
		assertEquals("http://www.apache.org/licenses/LICENSE-2.0.html", apiSource.getInfo().getLicense().getUrl());
		assertEquals("Apache 2.0", apiSource.getInfo().getLicense().getName());
	}
}
