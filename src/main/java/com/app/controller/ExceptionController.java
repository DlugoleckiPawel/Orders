package com.app.controller;

import com.app.dto.InfoDto;
import com.app.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
	@ExceptionHandler(AppException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public InfoDto handleException(AppException e) {
		return InfoDto
				.builder()
				.error(e.getMessage())
				.build();
	}
}
