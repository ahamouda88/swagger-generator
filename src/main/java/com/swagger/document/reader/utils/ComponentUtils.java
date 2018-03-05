package com.swagger.document.reader.utils;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.swagger.document.factory.ParamFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.parameters.Parameter;
import io.swagger.util.ParameterProcessor;

public interface ComponentUtils {

	static List<SecurityRequirement> getSecurityRequirements(Api api) {
		if (ArrayUtils.isEmpty(api.authorizations()))
			return newArrayList();

		return asList(api.authorizations()).stream().filter(a -> !a.value().isEmpty()).map(a -> {
			SecurityRequirement security = new SecurityRequirement();
			security.requirement(a.value());
			asList(a.scopes()).stream().filter(s -> !s.scope().isEmpty()).forEach(s -> {
				security.addScope(s.scope());
			});
			return security;
		}).collect(Collectors.toList());
	}
}
