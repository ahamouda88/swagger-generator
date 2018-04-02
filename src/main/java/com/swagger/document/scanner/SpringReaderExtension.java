package com.swagger.document.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import io.swagger.models.Operation;
import io.swagger.servlet.ReaderContext;
import io.swagger.servlet.extensions.ReaderExtension;

public class SpringReaderExtension implements ReaderExtension {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isReadable(ReaderContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void applyConsumes(ReaderContext context, Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyProduces(ReaderContext context, Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHttpMethod(ReaderContext context, Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath(ReaderContext context, Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyOperationId(Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applySummary(Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyDescription(Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applySchemes(ReaderContext context, Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDeprecated(Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applySecurityRequirements(ReaderContext context, Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyTags(ReaderContext context, Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyResponses(ReaderContext context, Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyParameters(ReaderContext context, Operation operation, Type type, Annotation[] annotations) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyImplicitParameters(ReaderContext context, Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyExtensions(ReaderContext context, Operation operation, Method method) {
		// TODO Auto-generated method stub
		
	}
	
}
