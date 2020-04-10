package com.bookstore.bookstoreapplication.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.OptimisticLockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author umeshkumar01
 *
 */
@ControllerAdvice
public class BookStoreInformationExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	Environment env;

	/**
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BookStoreInformationException.class)
	public ResponseEntity<BookStoreInformationExceptionConverter> displayBookStoreInformationException(BookStoreInformationException ex) {

		BookStoreInformationExceptionConverter aPIBookInformationException = new BookStoreInformationExceptionConverter();
		aPIBookInformationException.setErrorCode(ex.getErrorCode());
		aPIBookInformationException.setErrorMessage(ex.getErrorMessage());
		aPIBookInformationException.setTimeStamp(new Date().getTime());
		if (ex.getErrorCode().equals("400")) {
			return new ResponseEntity<BookStoreInformationExceptionConverter>(aPIBookInformationException,
					HttpStatus.BAD_REQUEST);

		}

		return new ResponseEntity<BookStoreInformationExceptionConverter>(aPIBookInformationException,
				HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> value = new HashMap<String, Object>();
		BookStoreInformationExceptionConverter aPIBookInformationException = new BookStoreInformationExceptionConverter();
		aPIBookInformationException.setTimeStamp(new Date().getTime());
		value.put("status", status.value());
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
				.collect(Collectors.toList());
		value.put("errors", errors);
		aPIBookInformationException.setErrorMap(value);
		return new ResponseEntity<Object>(aPIBookInformationException, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(OptimisticLockException.class)
	public ResponseEntity<BookStoreInformationExceptionConverter> displayOptimisticLockException(OptimisticLockException ex) {

		BookStoreInformationExceptionConverter aPIBookInformationException = new BookStoreInformationExceptionConverter();
		aPIBookInformationException.setErrorCode("409");
		aPIBookInformationException.setErrorMessage(env.getProperty("error.optimistic.lock"));
		aPIBookInformationException.setTimeStamp(new Date().getTime());
		return new ResponseEntity<BookStoreInformationExceptionConverter>(aPIBookInformationException, HttpStatus.CONFLICT);

	}


}
