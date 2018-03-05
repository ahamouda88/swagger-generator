package com.swagger.document.mavenplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

import io.swagger.models.Info;

public class ApiSource {

	@Parameter(required = true)
	private List<String> locations;

	@Parameter
	private Info info;

	@Parameter
	private String basePath;

	@Parameter
	private String host;

	@Parameter
	private List<String> schemes;

	@Parameter
	private String outputFormats;

	@Parameter
	private String swaggerDirectory;

	@Parameter
	private String swaggerFileName;

	@Parameter
	private boolean attachSwaggerArtifact;

	@Parameter
	private String modelSubstitute;

	@Parameter
	private String apiSortComparator;

	@Parameter
	private String swaggerInternalFilter;

	@Parameter
	private String swaggerApiReader;

	@Parameter
	private List<String> swaggerExtensions;

	@Parameter
	private String swaggerSchemaConverter;

	@Parameter
	private List<String> typesToSkip = new ArrayList<String>();

	@Parameter
	private List<String> apiModelPropertyAccessExclusions = new ArrayList<String>();

	@Parameter
	private boolean jsonExampleValues = false;

	@Parameter
	private File descriptionFile;

	@Parameter
	private List<String> modelConverters;

	// @Parameter
	// private List<SecurityDefinition> securityDefinitions;

	public List<String> getLocations() {
		return locations;
	}

	public Info getInfo() {
		return info;
	}

	public String getBasePath() {
		return basePath;
	}

	public String getHost() {
		return host;
	}

	public List<String> getSchemes() {
		return schemes;
	}

	public String getOutputFormats() {
		return outputFormats;
	}

	public String getSwaggerDirectory() {
		return swaggerDirectory;
	}

	public String getSwaggerFileName() {
		return swaggerFileName;
	}

	public boolean isAttachSwaggerArtifact() {
		return attachSwaggerArtifact;
	}

	public String getModelSubstitute() {
		return modelSubstitute;
	}

	public String getApiSortComparator() {
		return apiSortComparator;
	}

	public String getSwaggerInternalFilter() {
		return swaggerInternalFilter;
	}

	public String getSwaggerApiReader() {
		return swaggerApiReader;
	}

	public List<String> getSwaggerExtensions() {
		return swaggerExtensions;
	}

	public String getSwaggerSchemaConverter() {
		return swaggerSchemaConverter;
	}

	public List<String> getTypesToSkip() {
		return typesToSkip;
	}

	public List<String> getApiModelPropertyAccessExclusions() {
		return apiModelPropertyAccessExclusions;
	}

	public boolean isJsonExampleValues() {
		return jsonExampleValues;
	}

	public File getDescriptionFile() {
		return descriptionFile;
	}

	public List<String> getModelConverters() {
		return modelConverters;
	}
}
