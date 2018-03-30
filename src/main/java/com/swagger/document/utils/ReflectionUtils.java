package com.swagger.document.utils;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

public class ReflectionUtils {

    public static Set<Class<?>> getValidClasses(Class<? extends Annotation> clazz) {
        return getValidClasses(clazz, Lists.newArrayList(""));
    }

    public static Set<Class<?>> getValidClasses(Class<? extends Annotation> clazz,
                                                List<String> packages) {
    	
    		if(CollectionUtils.isEmpty(packages)) return null;

    		Set<Class<?>> classes = new LinkedHashSet<>();
        packages.stream().forEach(path -> {
            classes.addAll(new Reflections(path).getTypesAnnotatedWith(clazz, true));
        });
        return classes;
    }

    public static Set<Class<?>> getValidClasses(Set<Class<? extends Annotation>> annotations, List<String> packages) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        annotations.forEach(annotation -> {
            classes.addAll(getValidClasses(annotation, packages));
        });
        return classes;
    }
}
