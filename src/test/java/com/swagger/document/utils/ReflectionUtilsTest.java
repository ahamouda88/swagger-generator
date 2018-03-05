package com.swagger.document.utils;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.swagger.document.utils.ReflectionUtils;

public class ReflectionUtilsTest {

	@Test
	public void testGetValidClassesWithValidPackage() {
		Set<Class<?>> classes = ReflectionUtils.getValidClasses(RestController.class,
				Lists.newArrayList("com.groupon.utils"));
		Assert.assertTrue(classes.contains(ParentClass.class));
		Assert.assertTrue(classes.contains(ChildClass.class));
	}

	@Test
	public void testGetValidClassesWithInvalidPackage() {
		Set<Class<?>> classes = ReflectionUtils.getValidClasses(RestController.class,
				Lists.newArrayList("com.groupon.invalid.package"));
		Assert.assertTrue(classes.isEmpty());
	}

	@RestController
	public class ParentClass {
	}

	@RestController
	public class ChildClass extends ParentClass {

	}
}
