package com.groupon.swagger.plugin;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.groupon.swagger.common.BaseMavenPluginTest;
import com.swagger.document.mavenplugin.ApiSource;

public class ApiDocumentMojoTest extends BaseMavenPluginTest {

	@Test
	public void testApiSourceParameters() throws Exception {
		List<ApiSource> apiSources = mojo.getApiSources();
		assertEquals(1, apiSources.size());

		ApiSource apiSource = apiSources.get(0);
		assertEquals("/api", apiSource.getBasePath());
		assertEquals("www.groupon.com", apiSource.getHost());
		assertEquals("com.groupon.swagger", apiSource.getLocations().get(0));
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
