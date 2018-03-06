package com.swagger.document.mavenplugin;

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

}
