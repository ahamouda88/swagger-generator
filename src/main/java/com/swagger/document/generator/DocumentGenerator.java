package com.swagger.document.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.swagger.document.constants.ErrorMessages;
import com.swagger.document.mavenplugin.ApiSource;
import com.swagger.document.reader.utils.MainComponentUtils;
import com.swagger.document.scanner.DefaultScanner;
import com.swagger.document.utils.ReflectionUtils;

import io.swagger.annotations.Api;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;

import static com.swagger.document.utils.SplitterUtil.COMMA_SEP;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DocumentGenerator {
    protected final Log LOG = new DefaultLog(new ConsoleLogger());

    protected Swagger   swagger;

    public DocumentGenerator(ApiSource apiSource) {
        Preconditions.checkNotNull(apiSource, ErrorMessages.API_SOURCE_MISSING);

        Set<Class<?>> classes = getClasses(apiSource.getLocations());
        this.swagger = new Swagger();
        DefaultScanner.read(swagger, classes);

        overrideSwaggerDefinitionFields(apiSource);
    }

    /**
     * This method return a set of classes that has the resources, given the paths or locations of
     * these resources
     * 
     * @param locations
     * @return set of classes
     */
    private Set<Class<?>> getClasses(List<String> locations) {
        return ReflectionUtils.getValidClasses(Api.class, locations);
    }

    private void overrideSwaggerDefinitionFields(ApiSource apiSource) {
        if (isNotBlank(apiSource.getHost())) swagger.setHost(apiSource.getHost());

        if (isNotBlank(apiSource.getBasePath())) swagger.setBasePath(apiSource.getBasePath());

        if (apiSource.getInfo() != null) swagger.setInfo(apiSource.getInfo());
    }

    public void setSecurityRequirements(Api api) {
        List<SecurityRequirement> securityRequirementList =
                        MainComponentUtils.getSecurityRequirements(api);
        securityRequirementList.forEach(securityRequirement -> {
            swagger.addSecurity(securityRequirement);
        });
    }

    public Swagger getSwagger() {
        return swagger;
    }
}
