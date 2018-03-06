package com.swagger.document.reader;

import java.lang.reflect.Type;
import java.util.Set;

import com.google.common.collect.Sets;
import com.swagger.document.mavenplugin.ApiSource;

public class SpringDocumentReader extends DocumentReader {

    public SpringDocumentReader(ApiSource apiSource) {
        super(apiSource);
    }

    private final Set<Type> validAnnotations = Sets.newHashSet();

//    @Override
    public Set<Type> getValidAnnotations() {
        return validAnnotations;
    }
}
