package com.swagger.document.reader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import com.google.common.collect.Sets;
import com.swagger.document.factory.ParamFactory;
import com.swagger.document.mavenplugin.ApiSource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.annotations.ResponseHeader;
import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.util.ParameterProcessor;
import io.swagger.util.PathUtils;

public abstract class DocumentReader {
	private final static Set<String> PRIMITIVE_TYPES = Sets.newHashSet("integer", "string", "number", "boolean",
			"array", "file");
	protected final Log LOG = new DefaultLog(new ConsoleLogger());

	protected Swagger swagger;

	protected Swagger swaggerTemp;

	// private Set<Type> typesToSkip = new HashSet<Type>();
	//
	// public Set<Type> getTypesToSkip() {
	// return typesToSkip;
	// }
	//
	// public void setTypesToSkip(List<Type> typesToSkip) {
	// this.typesToSkip = new HashSet<Type>(typesToSkip);
	// }
	//
	// public void setTypesToSkip(Set<Type> typesToSkip) {
	// this.typesToSkip = typesToSkip;
	// }
	//
	// public void addTypeToSkippedTypes(Type type) {
	// this.typesToSkip.add(type);
	// }
	//
	public DocumentReader(ApiSource apiSource, Swagger swagger) {
		this.swagger = swagger;
		this.swaggerTemp = new Swagger();
		this.swaggerTemp.setInfo(apiSource.getInfo());
		this.swaggerTemp.setHost(apiSource.getHost());
		this.swaggerTemp.setBasePath(apiSource.getBasePath());
		updateExtensionChain();
	}

	/**
	 * Method which allows sub-classes to modify the Swagger extension chain.
	 */
	protected void updateExtensionChain() {
		// default implementation does nothing
	}

	// TODO: in swagger util


	protected String parseOperationPath(String operationPath, Map<String, String> regexMap) {
		return PathUtils.parsePath(operationPath, regexMap);
	}

	protected void updateOperationParameters(List<Parameter> parentParameters, Map<String, String> regexMap,
			Operation operation) {
		if (parentParameters != null) {
			for (Parameter param : parentParameters) {
				operation.parameter(param);
			}
		}
		for (Parameter param : operation.getParameters()) {
			String pattern = regexMap.get(param.getName());
			if (pattern != null) {
				param.setPattern(pattern);
			}
		}
	}

	protected Map<String, Property> parseResponseHeaders(ResponseHeader[] headers) {
		if (headers == null) {
			return null;
		}
		Map<String, Property> responseHeaders = null;
		for (ResponseHeader header : headers) {
			if (header.name().isEmpty()) {
				continue;
			}
			if (responseHeaders == null) {
				responseHeaders = new HashMap<String, Property>();
			}
			Class<?> cls = header.response();

			if (!cls.equals(Void.class) && !cls.equals(void.class)) {
				Property property = ModelConverters.getInstance().readAsProperty(cls);
				if (property != null) {
					Property responseProperty;

					if (header.responseContainer().equalsIgnoreCase("list")) {
						responseProperty = new ArrayProperty(property);
					} else if (header.responseContainer().equalsIgnoreCase("map")) {
						responseProperty = new MapProperty(property);
					} else {
						responseProperty = property;
					}
					responseProperty.setDescription(header.description());
					responseHeaders.put(header.name(), responseProperty);
				}
			}
		}
		return responseHeaders;
	}

	protected Set<Map<String, Object>> parseCustomExtensions(Extension[] extensions) {
		if (extensions == null) {
			return Collections.emptySet();
		}
		Set<Map<String, Object>> resultSet = new HashSet<Map<String, Object>>();
		for (Extension extension : extensions) {
			if (extension == null) {
				continue;
			}
			Map<String, Object> extensionProperties = new HashMap<String, Object>();
			for (ExtensionProperty extensionProperty : extension.properties()) {
				String name = extensionProperty.name();
				if (!name.isEmpty()) {
					String value = extensionProperty.value();
					extensionProperties.put(name, value);
				}
			}
			if (!extension.name().isEmpty()) {
				Map<String, Object> wrapper = new HashMap<String, Object>();
				wrapper.put(extension.name(), extensionProperties);
				resultSet.add(wrapper);
			} else {
				resultSet.add(extensionProperties);
			}
		}
		return resultSet;
	}

	protected void updatePath(String operationPath, String httpMethod, Operation operation) {
		if (httpMethod == null) {
			return;
		}
		Path path = swagger.getPath(operationPath);
		if (path == null) {
			path = new Path();
			swagger.path(operationPath, path);
		}
		path.set(httpMethod, operation);
	}

	protected void updateTagsForOperation(Operation operation, ApiOperation apiOperation) {
		if (apiOperation == null) {
			return;
		}
		for (String tag : apiOperation.tags()) {
			if (!tag.isEmpty()) {
				operation.tag(tag);
				swagger.tag(new Tag().name(tag));
			}
		}
	}

	protected boolean canReadApi(boolean readHidden, Api api) {
		return (api == null) || (readHidden) || (!api.hidden());
	}

	protected Set<Tag> extractTags(Api api) {
		Set<Tag> output = new LinkedHashSet<Tag>();
		if (api == null) {
			return output;
		}

		boolean hasExplicitTags = false;
		for (String tag : api.tags()) {
			if (!tag.isEmpty()) {
				hasExplicitTags = true;
				output.add(new Tag().name(tag));
			}
		}
		if (!hasExplicitTags) {
			// derive tag from api path + description
			String tagString = api.value().replace("/", "");
			if (!tagString.isEmpty()) {
				Tag tag = new Tag().name(tagString);
				if (!api.description().isEmpty()) {
					tag.description(api.description());
				}
				output.add(tag);
			}
		}
		return output;
	}

	protected void updateOperationProtocols(ApiOperation apiOperation, Operation operation) {
		if (apiOperation == null) {
			return;
		}
		String[] protocols = apiOperation.protocols().split(",");
		for (String protocol : protocols) {
			String trimmed = protocol.trim();
			if (!trimmed.isEmpty()) {
				operation.scheme(Scheme.forValue(trimmed));
			}
		}
	}

	protected Map<String, Tag> updateTagsForApi(Map<String, Tag> parentTags, Api api) {
		// the value will be used as a tag for 2.0 UNLESS a Tags annotation is present
		Map<String, Tag> tagsMap = new HashMap<String, Tag>();
		for (Tag tag : extractTags(api)) {
			tagsMap.put(tag.getName(), tag);
		}
		if (parentTags != null) {
			tagsMap.putAll(parentTags);
		}
		for (Tag tag : tagsMap.values()) {
			swagger.tag(tag);
		}
		return tagsMap;
	}

	protected boolean isPrimitive(Type cls) {
		Property property = ModelConverters.getInstance().readAsProperty(cls);
		return property == null ? false : PRIMITIVE_TYPES.contains(property.getType());
	}

	protected void updateOperation(String[] apiConsumes, String[] apiProduces, Map<String, Tag> tags,
			List<SecurityRequirement> securities, Operation operation) {
		if (operation == null) {
			return;
		}
		if (operation.getConsumes() == null) {
			for (String mediaType : apiConsumes) {
				operation.consumes(mediaType);
			}
		}
		if (operation.getProduces() == null) {
			for (String mediaType : apiProduces) {
				operation.produces(mediaType);
			}
		}

		if (operation.getTags() == null) {
			for (String tagString : tags.keySet()) {
				operation.tag(tagString);
			}
		}
		for (SecurityRequirement security : securities) {
			operation.security(security);
		}
	}

	private boolean isApiParamHidden(List<Annotation> parameterAnnotations) {
		for (Annotation parameterAnnotation : parameterAnnotations) {
			if (parameterAnnotation instanceof ApiParam) {
				return ((ApiParam) parameterAnnotation).hidden();
			}
		}

		return false;
	}

	private boolean hasValidAnnotations(List<Annotation> parameterAnnotations) {
		// Because method parameters can contain parameters that are valid, but
		// not part of the API contract, first check to make sure the parameter
		// has at lease one annotation before processing it. Also, check a
		// whitelist to make sure that the annotation of the parameter is
		// compatible with spring-maven-plugin

		List<Type> validParameterAnnotations = new ArrayList<Type>();
		validParameterAnnotations.add(ModelAttribute.class);
		validParameterAnnotations.add(ApiParam.class);
		validParameterAnnotations.add(PathParam.class);
		validParameterAnnotations.add(QueryParam.class);
		validParameterAnnotations.add(HeaderParam.class);
		validParameterAnnotations.add(FormParam.class);
		validParameterAnnotations.add(RequestParam.class);
		validParameterAnnotations.add(RequestBody.class);
		validParameterAnnotations.add(PathVariable.class);
		validParameterAnnotations.add(RequestHeader.class);
		validParameterAnnotations.add(RequestPart.class);
		validParameterAnnotations.add(CookieValue.class);

		boolean hasValidAnnotation = false;
		for (Annotation potentialAnnotation : parameterAnnotations) {
			if (validParameterAnnotations.contains(potentialAnnotation.annotationType())) {
				hasValidAnnotation = true;
				break;
			}
		}

		return hasValidAnnotation;
	}

	protected List<Parameter> getParameters(Type type, List<Annotation> annotations) {
		if (!hasValidAnnotations(annotations) || isApiParamHidden(annotations)) {
			return Collections.emptyList();
		}

		Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
		List<Parameter> parameters = new ArrayList<Parameter>();
		Class<?> cls = TypeUtils.getRawType(type, type);
		// LOG.debug("Looking for path/query/header/form/cookie params in " + cls);

		// if (chain.hasNext()) {
		// SwaggerExtension extension = chain.next();
		// LOG.debug("trying extension " + extension);
		// parameters = extension.extractParameters(annotations, type, typesToSkip,
		// chain);
		// }

		if (!parameters.isEmpty()) {
			for (Parameter parameter : parameters) {
				ParameterProcessor.applyAnnotations(swagger, parameter, type, annotations);
			}
		} else {
			// LOG.debug("Looking for body params in " + cls);
			// if (!typesToSkip.contains(type)) {
			// Parameter param = ParameterProcessor.applyAnnotations(swagger, null, type,
			// annotations);
			// if (param != null) {
			// parameters.add(param);
			// }
			// }
		}
		return parameters;
	}

	protected void updateApiResponse(Operation operation, ApiResponses responseAnnotation) {
		for (ApiResponse apiResponse : responseAnnotation.value()) {
			Map<String, Property> responseHeaders = parseResponseHeaders(apiResponse.responseHeaders());
			Class<?> responseClass = apiResponse.response();
			Response response = new Response().description(apiResponse.message()).headers(responseHeaders);

			if (responseClass.equals(Void.class)) {
				if (operation.getResponses() != null) {
					Response apiOperationResponse = operation.getResponses().get(String.valueOf(apiResponse.code()));
					if (apiOperationResponse != null) {
						response.setSchema(apiOperationResponse.getSchema());
					}
				}
			} else {
				Map<String, Model> models = ModelConverters.getInstance().read(responseClass);
				for (String key : models.keySet()) {
					final Property schema = new RefProperty().asDefault(key);
					if (apiResponse.responseContainer().equals("List")) {
						response.schema(new ArrayProperty(schema));
					} else {
						response.schema(schema);
					}
					swagger.model(key, models.get(key));
				}
				models = ModelConverters.getInstance().readAll(responseClass);
				for (Map.Entry<String, Model> entry : models.entrySet()) {
					swagger.model(entry.getKey(), entry.getValue());
				}

				if (response.getSchema() == null) {
					Map<String, Response> responses = operation.getResponses();
					if (responses != null) {
						Response apiOperationResponse = responses.get(String.valueOf(apiResponse.code()));
						if (apiOperationResponse != null) {
							response.setSchema(apiOperationResponse.getSchema());
						}
					}
				}
			}

			if (apiResponse.code() == 0) {
				operation.defaultResponse(response);
			} else {
				operation.response(apiResponse.code(), response);
			}
		}
	}

	private String[] updateOperationProduces(String[] parentProduces, String[] apiProduces, Operation operation) {
		if (parentProduces != null) {
			Set<String> both = new LinkedHashSet<String>(Arrays.asList(apiProduces));
			both.addAll(Arrays.asList(parentProduces));
			if (operation.getProduces() != null) {
				both.addAll(operation.getProduces());
			}
			apiProduces = both.toArray(new String[both.size()]);
		}
		return apiProduces;
	}

	private String[] updateOperationConsumes(String[] parentConsumes, String[] apiConsumes, Operation operation) {
		if (parentConsumes != null) {
			Set<String> both = new LinkedHashSet<String>(Arrays.asList(apiConsumes));
			both.addAll(Arrays.asList(parentConsumes));
			if (operation.getConsumes() != null) {
				both.addAll(operation.getConsumes());
			}
			apiConsumes = both.toArray(new String[both.size()]);
		}
		return apiConsumes;
	}

	protected void readImplicitParameters(Method method, Operation operation) {
		ApiImplicitParams implicitParams = AnnotationUtils.findAnnotation(method, ApiImplicitParams.class);
		if (implicitParams == null) {
			return;
		}
		for (ApiImplicitParam param : implicitParams.value()) {
			Class<?> cls;
			try {
				cls = Class.forName(param.dataType());
			} catch (ClassNotFoundException e) {
				cls = method.getDeclaringClass();
			}

			Parameter p = readImplicitParam(param, cls);
			if (p != null) {
				operation.addParameter(p);
			}
		}
	}

	private Parameter readImplicitParam(ApiImplicitParam param, Class<?> apiClass) {
		Parameter parameter = ParamFactory.createParameter(param);

		return ParameterProcessor.applyAnnotations(swagger, parameter, apiClass,
				Arrays.asList(new Annotation[] { param }));
	}

	void processOperationDecorator(Operation operation, Method method) {
		final Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
		if (chain.hasNext()) {
			SwaggerExtension extension = chain.next();
			extension.decorateOperation(operation, method, chain);
		}
	}
}