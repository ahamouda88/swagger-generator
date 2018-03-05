package com.swagger.document.exception;

public class SwaggerGenerationException extends RuntimeException {

	private static final long serialVersionUID = 224901338232927865L;

	public SwaggerGenerationException(String message) {
		super(message);
	}

	public SwaggerGenerationException(String message, Throwable cause) {
		super(message, cause);
	}
}
