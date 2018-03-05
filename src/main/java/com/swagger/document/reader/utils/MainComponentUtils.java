package com.swagger.document.reader.utils;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.models.SecurityRequirement;

public interface MainComponentUtils {
	
	static Set<Map<String, Object>> getCustomExtensions(Extension[] extensions) {
		if (isEmpty(extensions)) {
			return emptySet();
		}
		Set<Map<String, Object>> resultSet = new HashSet<>();
		for (Extension extension : extensions) {
			if (extension == null) {
				continue;
			}
			Map<String, Object> extensionProperties = new HashMap<>();
			for (ExtensionProperty extensionProperty : extension.properties()) {
				String name = extensionProperty.name();
				if (!name.isEmpty()) {
					extensionProperties.put(name, extensionProperty.value());
				}
			}
			if (!extension.name().isEmpty()) {
				Map<String, Object> wrapper = new HashMap<>();
				wrapper.put(extension.name(), extensionProperties);
				resultSet.add(wrapper);
			} else {
				resultSet.add(extensionProperties);
			}
		}
		return resultSet;
	}

	static List<SecurityRequirement> getSecurityRequirements(Api api) {
		if (isEmpty(api.authorizations()))
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
