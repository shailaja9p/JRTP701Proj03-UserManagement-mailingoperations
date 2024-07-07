package com.lifetree.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserErrorHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException unf){
		System.out.println("UserErrorHandler.handleUserNotFoundException()");
		ErrorDetails details= new ErrorDetails(LocalDateTime.now(),unf.getMessage(),"404-User Not Found");
		return new ResponseEntity<ErrorDetails>(details,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleException(Exception e){
		System.out.println("UserErrorHandler.handleException()");
		ErrorDetails details= new ErrorDetails(LocalDateTime.now(),e.getMessage(),"Problem in Execution");
		return new ResponseEntity<ErrorDetails>(details,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
