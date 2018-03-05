package com.swagger.document.reader;

import java.lang.reflect.Type;
import java.util.Set;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import com.google.common.collect.Sets;
import com.swagger.document.mavenplugin.ApiSource;

import io.swagger.annotations.ApiParam;
import io.swagger.models.Swagger;

public class SpringDocumentReader extends DocumentReader {

	public SpringDocumentReader(ApiSource apiSource, Swagger swagger) {
		super(apiSource, swagger);
	}

	private final Set<Type> validAnnotations = Sets.newHashSet(ModelAttribute.class, ApiParam.class, RequestParam.class,
			RequestBody.class, PathVariable.class, RequestPart.class, CookieValue.class, RequestHeader.class);

	private Set<Type> getValidAnnotations() {
		return validAnnotations;
	}
}
