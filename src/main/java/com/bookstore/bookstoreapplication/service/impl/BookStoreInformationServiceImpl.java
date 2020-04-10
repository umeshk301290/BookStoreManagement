package com.bookstore.bookstoreapplication.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.bookstoreapplication.domain.BookStoreInformationDomain;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;
import com.bookstore.bookstoreapplication.service.BookStoreInformationService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author umeshkumar01
 *
 */
@Service
@Slf4j
public class BookStoreInformationServiceImpl implements BookStoreInformationService {

	@Autowired
	BookStoreInformationRepository bookInformationRepository;

	@Autowired
	BookStoreInformationDomain bookStoreInformationBusiness;

	@Autowired
	Environment env;

	/**
	 * @param book
	 * @return
	 * @throws BookStoreInformationException
	 */
	@Transactional
	public ResponseEntity<BookStoreInformation> addBookInformation(BookStoreInformation book)
			throws BookStoreInformationException {
		// TODO Auto-generated method stub

		BookStoreInformation bookStoreInformation = bookInformationRepository.findbyIsbn(book.getIsbn()).orElse(null);
		log.info("book information found is {} ",bookStoreInformation);
		ResponseEntity<BookStoreInformation> bookStoreInformationResponse = bookStoreInformationBusiness
				.addBookInformation(bookStoreInformation, book);

		return bookStoreInformationResponse;
	}

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	@Transactional
	public ResponseEntity<BookStoreInformation> fetchBookInformation(String isbn) throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation bookInformation = bookInformationRepository.findbyIsbn(isbn).orElseThrow(
				() -> new BookStoreInformationException(env.getProperty("error.book.isbn.not.found.message"),
						env.getProperty("error.book.isbn.not.found.code")));

		return ResponseEntity.ok().body(bookInformation);

	}

	/**
	 * @param title
	 * @return
	 * @throws BookStoreInformationException
	 */
	public ResponseEntity<List<BookStoreInformation>> fetchBookInformationFromTitle(String title)
			throws BookStoreInformationException {
		// TODO Auto-generated method stub
		List<BookStoreInformation> bookInformationList = bookInformationRepository.findbyTitle(title);
		log.info("books found with title {}" ,bookInformationList );
		return ResponseEntity.ok().body(bookInformationList);

	}

	/**
	 * @param author
	 * @return
	 */
	public ResponseEntity<List<BookStoreInformation>> fetchBookInformationFromAuthor(String author) {
		// TODO Auto-generated method stub
		List<BookStoreInformation> bookInformationList = bookInformationRepository.findbyAuthor(author);
		log.info("books found with author {}" ,bookInformationList );
		return ResponseEntity.ok().body(bookInformationList);
	}

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	public ResponseEntity<List<String>> searchMediaCoverage(String isbn) throws BookStoreInformationException {
		// TODO Auto-generated method stub
		ResponseEntity<List<String>> responseList = bookStoreInformationBusiness.searchMediaCoverage(isbn);
		if (Objects.nonNull(responseList) && responseList.getBody().size() > 0) {
			log.info("mediacoverage for the specified book is {} ",responseList.getBody());
			return ResponseEntity.ok().body(responseList.getBody());
		} else {
			throw new BookStoreInformationException(env.getProperty("error.media.coverage.not.found.message"),
					env.getProperty("error.media.coverage.not.found.code"));

		}

	}

	/**
	 * @param isbn
	 * @param quantity
	 * @return
	 * @throws BookStoreInformationException
	 */
	@Transactional
	public ResponseEntity<BookStoreInformation> purchaseBook(String isbn, Integer quantity)
			throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation buyBookInformation = bookInformationRepository.findbyIsbn(isbn).orElseThrow(
				() -> new BookStoreInformationException(env.getProperty("error.book.isbn.not.found.message"),
						env.getProperty("error.book.isbn.not.found.code")));
		BookStoreInformation bookInformationPresent = bookStoreInformationBusiness.purchaseBook(buyBookInformation,
				quantity);
		return ResponseEntity.noContent().build();

	}

}
