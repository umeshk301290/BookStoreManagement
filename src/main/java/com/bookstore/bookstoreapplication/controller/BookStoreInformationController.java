package com.bookstore.bookstoreapplication.controller;

import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;
import com.bookstore.bookstoreapplication.service.BookStoreInformationService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author umeshkumar01
 *
 */
@RestController
@Slf4j
@RequestMapping(value = "bookstore")
public class BookStoreInformationController {

	@Autowired
	BookStoreInformationRepository bookInformationRepository;

	@Autowired
	BookStoreInformationService bookInformationService;

	@Autowired
	Environment env;

	/**
	 * @param eTagHeader
	 * @param book
	 * @return
	 * @throws BookStoreInformationException
	 */
	@PostMapping(value = "/add/book")
	public ResponseEntity<BookStoreInformation> addBookInformation(
			@RequestHeader(value = "If-Match", required = false) String eTagHeader,
			@Valid @RequestBody BookStoreInformation book) throws BookStoreInformationException {
		log.info("going to add a book for isbn {} , title {} , author {}", book.getIsbn(), book.getTitle(),
				book.getAuthor());
		BookStoreInformation bookStoreInformation = bookInformationService.checkForAlreadyPresentBookForAdd(eTagHeader,
				book);
		if (Objects.nonNull(bookStoreInformation)) {
			log.info("book is already present in bookstore");
			return ResponseEntity.ok().eTag(bookStoreInformation.getVersion().toString()).body(bookStoreInformation);
		} else {
			log.info("book is not present before in bookstore");
			if (Objects.isNull(book.getNumberOfCopies())) {
				book.setNumberOfCopies(1);
			}

			BookStoreInformation bookInformation = bookInformationRepository.save(book);
			return new ResponseEntity<BookStoreInformation>(bookInformation, HttpStatus.CREATED);

		}

	}

	/**
	 * @param eTagHeader
	 * @param book
	 * @return
	 * @throws BookStoreInformationException
	 */
	@PutMapping(value = "/update/book")
	public ResponseEntity<BookStoreInformation> updateBookInformation(@RequestHeader("If-Match") String eTagHeader,
			@Valid @RequestBody BookStoreInformation book) throws BookStoreInformationException {
		log.info("going to update  book for isbn {} , title {} , author {}", book.getIsbn(), book.getTitle(),
				book.getAuthor());

		BookStoreInformation updateBookStoreInformation = bookInformationRepository.findbyIsbn(book.getIsbn())
				.orElseThrow(
						() -> new BookStoreInformationException(env.getProperty("error.book.isbn.not.found.message"),
								env.getProperty("error.book.isbn.not.found.code")));

		if (!(eTagHeader.equals(String.valueOf(updateBookStoreInformation.getVersion())))) {
			throw new BookStoreInformationException(env.getProperty("error.book.incorrect.etag.message"),
					env.getProperty("error.book.incorrect.etag.code"));
		}
		updateBookStoreInformation.setAuthor(book.getAuthor());
		updateBookStoreInformation.setPrice(book.getPrice());
		updateBookStoreInformation.setTitle(book.getTitle());
		if (Objects.nonNull(book.getNumberOfCopies())) {
			updateBookStoreInformation.setNumberOfCopies(book.getNumberOfCopies());
		}
		bookInformationRepository.save(updateBookStoreInformation);
		return ResponseEntity.ok().eTag(updateBookStoreInformation.getVersion().toString())
				.body(updateBookStoreInformation);
	}

	/**
	 * @return
	 */
	@GetMapping(value = "/get/books")
	public ResponseEntity<List<BookStoreInformation>> fetchAllBooks() {
		List<BookStoreInformation> bookList = bookInformationRepository.findAll();
		return ResponseEntity.ok().body(bookList);

	}

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	@GetMapping(value = "/get/book/{isbn}")
	public ResponseEntity<BookStoreInformation> fetchBookBasedOnIsbn(@PathVariable("isbn") String isbn)
			throws BookStoreInformationException {
		BookStoreInformation bookInformation = bookInformationRepository.findbyIsbn(isbn).orElseThrow(
				() -> new BookStoreInformationException(env.getProperty("error.book.isbn.not.found.message"),
						env.getProperty("error.book.isbn.not.found.code")));

		return ResponseEntity.ok().eTag(bookInformation.getVersion().toString()).body(bookInformation);

	}

	/**
	 * @param title
	 * @return
	 * @throws BookStoreInformationException
	 */
	@GetMapping(value = "/get/books/title/{title}")
	public ResponseEntity<List<BookStoreInformation>> fetchBookBasedOnTitle(@PathVariable("title") String title)
			throws BookStoreInformationException {
		List<BookStoreInformation> bookInformationList = bookInformationRepository.findbyTitle(title).orElseThrow(
				() -> new BookStoreInformationException(env.getProperty("error.book.title.not.found.message"),
						env.getProperty("error.book.title.not.found.code")));

		return ResponseEntity.ok().body(bookInformationList);

	}

	/**
	 * @param author
	 * @return
	 * @throws BookStoreInformationException
	 */
	@GetMapping(value = "/get/books/author/{author}")
	public ResponseEntity<List<BookStoreInformation>> fetchBookBasedOnAuthor(@PathVariable("author") String author)
			throws BookStoreInformationException {
		List<BookStoreInformation> bookInformationList = bookInformationRepository.findbyAuthor(author).orElseThrow(
				() -> new BookStoreInformationException(env.getProperty("error.book.author.not.found.message"),
						env.getProperty("error.book.author.not.found.code")));

		return ResponseEntity.ok().body(bookInformationList);

	}
	
	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	@DeleteMapping(value = "/delete/book/{isbn}")
	public ResponseEntity removeBook(@PathVariable("isbn") String isbn)
			throws BookStoreInformationException {
		BookStoreInformation bookInformation = bookInformationRepository.findbyIsbn(isbn).orElseThrow(
				() -> new BookStoreInformationException(env.getProperty("error.book.isbn.not.found.message"),
						env.getProperty("error.book.isbn.not.found.code")));
		bookInformationRepository.deleteById(bookInformation.getId());  
		return ResponseEntity.ok().build();

	}

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	@GetMapping(value = "/mediacoverage/{isbn}")
	public ResponseEntity<List<String>> searchMediaCoverage(@PathVariable("isbn") String isbn)
			throws BookStoreInformationException {
		ResponseEntity<List<String>> responseList = bookInformationService.searchMediaCoverage(isbn);
		if (Objects.nonNull(responseList) && responseList.getBody().size() > 0) {
			return ResponseEntity.ok().body(responseList.getBody());
		} else {
			throw new BookStoreInformationException(env.getProperty("error.media.coverage.not.found.message"),
					env.getProperty("error.media.coverage.not.found.code"));

		}

	}

	/**
	 * @param eTagHeader
	 * @param isbn
	 * @param quantity
	 * @return
	 * @throws BookStoreInformationException
	 */
	@PatchMapping(value = "/buy/book/{isbn}")
	public ResponseEntity<BookStoreInformation> buyBook(@RequestHeader("If-Match") String eTagHeader,
			@PathVariable("isbn") String isbn,@RequestParam("quantity") Integer quantity) throws BookStoreInformationException {
		log.info("going to buy {} copies of book for isbn {}", quantity, isbn);
		
		BookStoreInformation buyBookInformation = bookInformationRepository.findbyIsbn(isbn).orElseThrow(
				() -> new BookStoreInformationException(env.getProperty("error.book.isbn.not.found.message"),
						env.getProperty("error.book.isbn.not.found.code")));
		BookStoreInformation bookInformationPresent = bookInformationService.checkForAlreadyPresentBookForBuy(eTagHeader,buyBookInformation,quantity);
		if(Objects.nonNull(bookInformationPresent)) {
		return ResponseEntity.noContent().eTag(bookInformationPresent.getVersion().toString()).build();
		}
		else {
			return ResponseEntity.noContent().build();

		}
	}

}
