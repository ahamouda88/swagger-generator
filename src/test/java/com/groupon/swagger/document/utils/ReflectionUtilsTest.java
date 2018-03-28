package com.groupon.swagger.document.utils;

import java.util.Set;

import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.swagger.document.utils.ReflectionUtils;

import io.swagger.annotations.Api;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ReflectionUtilsTest {

    private final static String INVALID_PACKAGE_PATH = "com.groupon.invalid.package",
                    VALID_PACKAGE_PATH = "com.groupon.swagger.document.utils";

    @Test
    public void testGetValidClassesWithValidPackage() {
        Set<Class<?>> classes = ReflectionUtils.getValidClasses(RestController.class,
                                                                Lists.newArrayList(VALID_PACKAGE_PATH));
        assertTrue(classes.contains(ParentClass.class));
        assertTrue(classes.contains(ChildClass.class));
    }

    @Test
    public void testGetValidClassesWithInvalidPackage() {
        Set<Class<?>> classes = ReflectionUtils.getValidClasses(RestController.class,
                                                                Lists.newArrayList(INVALID_PACKAGE_PATH));
        assertTrue(classes.isEmpty());
    }

    @Test
    public void testGetValidClassesWithValidPackageAndMultiAnnotations() {
        @SuppressWarnings("unchecked")
        Set<Class<?>> classes =
                        ReflectionUtils.getValidClasses(Sets.newHashSet(RestController.class, Api.class),
                                                        Lists.newArrayList(VALID_PACKAGE_PATH));
        assertEquals(classes.size(), 3);
        assertTrue(classes.contains(ChildClass.class));
        assertTrue(classes.contains(ParentClass.class));
        assertTrue(classes.contains(SwaggerClass.class));
    }

    @RestController
    public class ParentClass {
    }

    @RestController
    public class ChildClass extends ParentClass {

    }

    @Api
    public class SwaggerClass {

    }
}
