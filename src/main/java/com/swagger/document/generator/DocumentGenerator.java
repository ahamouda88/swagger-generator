package com.swagger.document.generator;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;

import com.google.common.base.Preconditions;
import com.swagger.document.constants.ErrorMessages;
import com.swagger.document.mavenplugin.ApiSource;
import com.swagger.document.reader.utils.MainComponentUtils;
import com.swagger.document.scanner.DefaultScanner;
import com.swagger.document.utils.ReflectionUtils;

import io.swagger.annotations.Api;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;

public class DocumentGenerator {
	protected final Log LOG = new DefaultLog(new ConsoleLogger());

	protected Swagger swagger;

	public DocumentGenerator(ApiSource apiSource) {
		Preconditions.checkNotNull(apiSource, ErrorMessages.API_SOURCE_MISSING);

		loadSwaggerDocument(apiSource, getClasses(apiSource.getLocations()));
	}
	
	public DocumentGenerator(ApiSource apiSource, Set<Class<?>> classes) {
		Preconditions.checkNotNull(apiSource, ErrorMessages.API_SOURCE_MISSING);

		loadSwaggerDocument(apiSource, classes);
	}

	/**
	 * This method return a set of classes that has the resources, given the paths
	 * or locations of these resources
	 * 
	 * @param locations
	 * @return set of classes
	 */
	public static Set<Class<?>> getClasses(List<String> locations) {
		return ReflectionUtils.getValidClasses(Api.class, locations);
	}

	private void overrideSwaggerDefinitionFields(ApiSource apiSource) {
		if (isNotBlank(apiSource.getHost()))
			swagger.setHost(apiSource.getHost());

		if (isNotBlank(apiSource.getBasePath()))
			swagger.setBasePath(apiSource.getBasePath());

		if (apiSource.getInfo() != null)
			swagger.setInfo(apiSource.getInfo());

		if (!isEmpty(apiSource.getSchemes()))
			swagger.setSchemes(mapToSchemes(apiSource.getSchemes()));
	}

	public void setSecurityRequirements(Api api) {
		List<SecurityRequirement> securityRequirementList = MainComponentUtils.getSecurityRequirements(api);
		securityRequirementList.forEach(securityRequirement -> {
			swagger.addSecurity(securityRequirement);
		});
	}

	public Swagger getSwagger() {
		return swagger;
	}

	private List<Scheme> mapToSchemes(List<String> schemes) {
		return schemes.stream().map(Scheme::forValue).collect(Collectors.toList());
	}

	private void loadSwaggerDocument(ApiSource apiSource, Set<Class<?>> classes) {
		this.swagger = new Swagger();
		DefaultScanner.read(swagger, classes);

		overrideSwaggerDefinitionFields(apiSource);
	}
}
