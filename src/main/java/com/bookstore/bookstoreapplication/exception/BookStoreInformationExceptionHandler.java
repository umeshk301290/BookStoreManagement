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
	public ResponseEntity<APIBookStoreInformationException> displayBookStoreInformationException(BookStoreInformationException ex) {

		APIBookStoreInformationException aPIBookInformationException = new APIBookStoreInformationException();
		aPIBookInformationException.setErrorCode(ex.getErrorCode());
		aPIBookInformationException.setErrorMessage(ex.getErrorMessage());
		aPIBookInformationException.setTimeStamp(new Date().getTime());
		if (ex.getErrorCode().equals("400")) {
			return new ResponseEntity<APIBookStoreInformationException>(aPIBookInformationException,
					HttpStatus.BAD_REQUEST);

		}
		if (ex.getErrorCode().equals("412")) {
			return new ResponseEntity<APIBookStoreInformationException>(aPIBookInformationException,
					HttpStatus.PRECONDITION_FAILED);

		}
		return new ResponseEntity<APIBookStoreInformationException>(aPIBookInformationException,
				HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> value = new HashMap<String, Object>();
		APIBookStoreInformationException aPIBookInformationException = new APIBookStoreInformationException();
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
	public ResponseEntity<APIBookStoreInformationException> displayOptimisticLockException(OptimisticLockException ex) {

		APIBookStoreInformationException aPIBookInformationException = new APIBookStoreInformationException();
		aPIBookInformationException.setErrorCode("409");
		aPIBookInformationException.setErrorMessage(ex.getMessage());
		aPIBookInformationException.setTimeStamp(new Date().getTime());
		return new ResponseEntity<APIBookStoreInformationException>(aPIBookInformationException, HttpStatus.CONFLICT);

	}

}
