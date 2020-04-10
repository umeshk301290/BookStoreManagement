package com.bookstore.bookstoreapplication.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;
import com.bookstore.bookstoreapplication.service.BookStoreInformationService;
import com.bookstore.bookstoreapplication.service.impl.BookStoreInformationServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author umeshkumar01
 *
 */
@RestController
@Slf4j
public class BookStoreInformationController {



	@Autowired
	BookStoreInformationService bookInformationService;

	/**
	 * @param book
	 * @return
	 * @throws BookStoreInformationException
	 */
	@PostMapping(value = "/books")
	public ResponseEntity<BookStoreInformation> addBookInformation(@Valid @RequestBody BookStoreInformation book)
			throws BookStoreInformationException {
		log.info("going to add a book for isbn {} , title {} , author {}", book.getIsbn(), book.getTitle(),
				book.getAuthor());
		ResponseEntity<BookStoreInformation> bookStoreInformationResponse = bookInformationService
				.addBookInformation(book);
		return bookStoreInformationResponse;
	}

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	@GetMapping(value = "/books/{isbn}")
	public ResponseEntity<BookStoreInformation> fetchBookBasedOnIsbn(@PathVariable("isbn") String isbn)
			throws BookStoreInformationException {
		ResponseEntity<BookStoreInformation> bookStoreInformationResponse = bookInformationService
				.fetchBookInformation(isbn);
		return bookStoreInformationResponse;
	}

	/**
	 * @param title
	 * @return
	 * @throws BookStoreInformationException
	 */
	@GetMapping(value = "books/titles/{title}")
	public ResponseEntity<List<BookStoreInformation>> fetchBookBasedOnTitle(@PathVariable("title") String title)
			throws BookStoreInformationException {
		ResponseEntity<List<BookStoreInformation>> bookStoreInformationResponse = bookInformationService
				.fetchBookInformationFromTitle(title);
		return bookStoreInformationResponse;
	}

	/**
	 * @param author
	 * @return
	 * @throws BookStoreInformationException
	 */
	@GetMapping(value = "books/authors/{author}")
	public ResponseEntity<List<BookStoreInformation>> fetchBookBasedOnAuthor(@PathVariable("author") String author)
			throws BookStoreInformationException {
		ResponseEntity<List<BookStoreInformation>> bookStoreInformationResponse = bookInformationService
				.fetchBookInformationFromAuthor(author);
		return bookStoreInformationResponse;

	}

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	@GetMapping(value = "books/mediacoverages/{isbn}")
	public ResponseEntity<List<String>> searchMediaCoverage(@PathVariable("isbn") String isbn)
			throws BookStoreInformationException {
		ResponseEntity<List<String>> responseList = bookInformationService.searchMediaCoverage(isbn);
		return responseList;

	}

	/**
	 * @param isbn
	 * @param quantity
	 * @return
	 * @throws BookStoreInformationException
	 */
	@PatchMapping(value = "books/{isbn}")
	public ResponseEntity<BookStoreInformation> buyBook(@PathVariable("isbn") String isbn,
			@RequestParam("quantity") Integer quantity) throws BookStoreInformationException {
		log.info("going to buy {} copies of book for isbn {}", quantity, isbn);
		ResponseEntity<BookStoreInformation> bookStoreInformationResponse = bookInformationService.purchaseBook(isbn,
				quantity);

		return bookStoreInformationResponse;
	}
}
