package com.swagger.document.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.base.Preconditions;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;

public final class ParamFactory {

	private final static Map<String, Supplier<Parameter>> PARAMETER_MAP;

	static {
		PARAMETER_MAP = new HashMap<>();
		PARAMETER_MAP.put("path", () -> new PathParameter());
		PARAMETER_MAP.put("query", () -> new QueryParameter());
		PARAMETER_MAP.put("form", () -> new FormParameter());
		PARAMETER_MAP.put("formData", () -> new FormParameter());
		PARAMETER_MAP.put("body", () -> new BodyParameter());
		PARAMETER_MAP.put("header", () -> new HeaderParameter());
	}

	public static Parameter createParameter(ApiImplicitParam param) {
		Preconditions.checkNotNull(param);

		Supplier<Parameter> supplier = PARAMETER_MAP.get(param.paramType());
		return supplier == null ? null : supplier.get();
	}
}
