package com.swagger.document.generator;

import java.lang.reflect.Type;
import java.util.Set;

import com.google.common.collect.Sets;
import com.swagger.document.mavenplugin.ApiSource;

public class SpringDocumentGenerator extends DocumentGenerator {

    public SpringDocumentGenerator(ApiSource apiSource) {
        super(apiSource);
    }

    private final Set<Type> validAnnotations = Sets.newHashSet();

//    @Override
    public Set<Type> getValidAnnotations() {
        return validAnnotations;
    }
}
