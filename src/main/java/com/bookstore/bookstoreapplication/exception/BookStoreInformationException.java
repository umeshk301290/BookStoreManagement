package com.bookstore.bookstoreapplication.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author umeshkumar01
 *
 */
@Getter
@Setter
public class BookStoreInformationException extends Exception {

	private static final long serialVersionUID = -5485553075889191833L;
	String errorMessage;
	String errorCode;
	
	public BookStoreInformationException(String errorMessage,String errorCode){
		this.errorMessage= errorMessage;
		this.errorCode = errorCode;
		
	}

}
