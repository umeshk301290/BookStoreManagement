package com.bookstore.bookstoreapplication.buisness;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;

public interface BookStoreInformationBusiness {

	/**
	 * @param bookStoreInformation
	 * @param book
	 * @return
	 * @throws BookStoreInformationException
	 */
	public ResponseEntity<BookStoreInformation> addBookInformation(BookStoreInformation bookStoreInformation,
			BookStoreInformation book) throws BookStoreInformationException;

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	
	public ResponseEntity<List<String>> searchMediaCoverage(@PathVariable("isbn") String isbn)
			throws BookStoreInformationException;

	/**
	 * @param buyBookInformation
	 * @param quantity
	 * @return
	 * @throws BookStoreInformationException
	 */
	public BookStoreInformation purchaseBook(BookStoreInformation buyBookInformation, Integer quantity)
			throws BookStoreInformationException;
	// TODO Auto-generated method stub

}
