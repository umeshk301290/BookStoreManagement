package com.bookstore.bookstoreapplication.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;

public interface BookStoreInformationService {

	/**
	 * @param book
	 * @return
	 * @throws BookStoreInformationException
	 */
	public ResponseEntity<BookStoreInformation> addBookInformation(BookStoreInformation book)
			throws BookStoreInformationException;

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	public ResponseEntity<BookStoreInformation> fetchBookInformation(String isbn) throws BookStoreInformationException;

	/**
	 * @param title
	 * @return
	 * @throws BookStoreInformationException
	 */
	public ResponseEntity<List<BookStoreInformation>> fetchBookInformationFromTitle(String title)
			throws BookStoreInformationException;

	/**
	 * @param author
	 * @return
	 */
	public ResponseEntity<List<BookStoreInformation>> fetchBookInformationFromAuthor(String author);

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	public ResponseEntity<List<String>> searchMediaCoverage(String isbn) throws BookStoreInformationException;
	// TODO Auto-generated method stub

	/**
	 * @param isbn
	 * @param quantity
	 * @return
	 * @throws BookStoreInformationException
	 */
	public ResponseEntity<BookStoreInformation> purchaseBook(String isbn, Integer quantity)
			throws BookStoreInformationException;

}