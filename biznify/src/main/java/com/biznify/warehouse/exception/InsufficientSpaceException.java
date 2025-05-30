package com.biznify.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InsufficientSpaceException extends Exception {
	public InsufficientSpaceException(String message) {
		super(message);
	}
}
