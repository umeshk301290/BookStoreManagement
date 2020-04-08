package com.bookstore.bookstoreapplication.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;
import com.bookstore.bookstoreapplication.service.BookStoreInformationService;

@RunWith(MockitoJUnitRunner.class)
public class BookStoreInformationControllerTest {

	@InjectMocks
	BookStoreInformationController bookStoreInformationController;

	@Mock
	BookStoreInformationRepository bookInformationRepository;

	@Mock
	BookStoreInformationService bookInformationService;

	@Mock
	Environment env;

	@Test
	public void addDuplicateBookInformationTest() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setNumberOfCopies(4);
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setAuthor("Henry");
		newBookInformation.setIsbn("123-45");
		newBookInformation.setNumberOfCopies(2);
		newBookInformation.setVersion(2L);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(newBookInformation,
				HttpStatus.OK);
		when(bookInformationService.checkForAlreadyPresentBookForAdd("abc",information)).thenReturn(newBookInformation);
		assertTrue(bookStoreInformationController.addBookInformation("abc",information) instanceof ResponseEntity);

	}

	@Test
	public void addBookInformationTest() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setAuthor("Henry");
		newBookInformation.setIsbn("123-45");
		newBookInformation.setNumberOfCopies(1);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(newBookInformation,
				HttpStatus.CREATED);
		when(bookInformationService.checkForAlreadyPresentBookForAdd("abc",information)).thenReturn(null);
		assertTrue(bookStoreInformationController.addBookInformation("abc",information) instanceof ResponseEntity);

	}

	@Test
	public void updateBookInformationTest() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setVersion(1L);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(information,
				HttpStatus.OK);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(information));
		assertTrue(bookStoreInformationController.updateBookInformation("1",information) instanceof ResponseEntity);

	}
	
	@Test(expected = BookStoreInformationException.class)
	public void updateBookInformationWithPreviousTest() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setVersion(2L);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(information,
				HttpStatus.OK);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(information));
		bookStoreInformationController.updateBookInformation("1",information);

	}

	@Test
	public void fetchAllBooksTest() {
		List<BookStoreInformation> bookList = new ArrayList<BookStoreInformation>();
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(4);

		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setAuthor("Matt");
		newBookInformation.setIsbn("123-46");
		information.setTitle("Maths");

		newBookInformation.setNumberOfCopies(1);
		bookList.add(information);
		bookList.add(newBookInformation);
		when(bookInformationRepository.findAll()).thenReturn(bookList);
		assertNotNull(bookStoreInformationController.fetchAllBooks());

	}

	@Test
	public void fetchBookBasedOnIsbn() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(4);
		information.setVersion(1L);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(information));
		assertTrue(bookStoreInformationController.fetchBookBasedOnIsbn("123-23") instanceof ResponseEntity);

	}

	@Test(expected = BookStoreInformationException.class)
	public void fetchBookBasedOnIsbnException() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(4);

		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.empty());
		bookStoreInformationController.fetchBookBasedOnIsbn("123-23");

	}

	@Test
	public void fetchBookBasedOnAuthor() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(4);

		BookStoreInformation bookInformation = new BookStoreInformation();
		bookInformation.setAuthor("Henry");
		bookInformation.setIsbn("123-45");
		bookInformation.setTitle("Maths");
		bookInformation.setNumberOfCopies(4);

		List<BookStoreInformation> bookInformationList = new ArrayList<BookStoreInformation>();
		bookInformationList.add(information);
		bookInformationList.add(bookInformation);

		when(bookInformationRepository.findbyAuthor(any())).thenReturn(Optional.of(bookInformationList));
		assertTrue(bookStoreInformationController.fetchBookBasedOnAuthor("williams") instanceof ResponseEntity);

	}

	@Test(expected = BookStoreInformationException.class)
	public void fetchBookBasedOnAuthorException() throws BookStoreInformationException {

		when(bookInformationRepository.findbyAuthor(any())).thenReturn(Optional.empty());
		bookStoreInformationController.fetchBookBasedOnAuthor("williams");

	}
	@Test
	public void fetchBookBasedOnTitle() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(4);

		BookStoreInformation bookInformation = new BookStoreInformation();
		bookInformation.setAuthor("Henry");
		bookInformation.setIsbn("123-45");
		bookInformation.setTitle("Maths");
		bookInformation.setNumberOfCopies(3);

		List<BookStoreInformation> bookInformationList = new ArrayList<BookStoreInformation>();
		bookInformationList.add(information);
		bookInformationList.add(bookInformation);

		when(bookInformationRepository.findbyTitle(any())).thenReturn(Optional.of(bookInformationList));
		assertTrue(bookStoreInformationController.fetchBookBasedOnTitle("maths") instanceof ResponseEntity);

	}

	@Test(expected = BookStoreInformationException.class)
	public void fetchBookBasedOnTitleException() throws BookStoreInformationException {

		when(bookInformationRepository.findbyTitle(any())).thenReturn(Optional.empty());
		bookStoreInformationController.fetchBookBasedOnTitle("maths");

	}
	@Test
	public void searchMediaCoverageTest() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(3);
		List<String> titleList = Arrays.asList("Maths", "English");
		ResponseEntity<List<String>> response = new ResponseEntity<List<String>>(titleList, HttpStatus.OK);
		when(bookInformationService.searchMediaCoverage(any())).thenReturn(response);
		assertTrue(bookStoreInformationController.searchMediaCoverage("123-45") instanceof ResponseEntity);

	}

	@Test(expected = BookStoreInformationException.class)
	public void searchMediaCoverageTestException() throws BookStoreInformationException {
		ResponseEntity<List<String>> response = null;
		when(bookInformationService.searchMediaCoverage(any())).thenReturn(response);
		bookStoreInformationController.searchMediaCoverage("123-45");

	}

	@Test
	public void buyBook() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setAuthor("Henry");
		newBookInformation.setIsbn("123-45");
		newBookInformation.setNumberOfCopies(1);
		newBookInformation.setVersion(1L);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(newBookInformation,
				HttpStatus.OK);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(newBookInformation));
		when(bookInformationService.checkForAlreadyPresentBookForBuy(any(),any(),any())).thenReturn(newBookInformation);
		assertTrue(bookStoreInformationController.buyBook("1",information.getIsbn(),1) instanceof ResponseEntity);

	}

	@Test(expected = BookStoreInformationException.class)
	public void buyBookExceptionTest() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setAuthor("Henry");
		newBookInformation.setIsbn("123-45");
		newBookInformation.setNumberOfCopies(1);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.empty());
		bookStoreInformationController.buyBook("1",information.getIsbn(),1);

	}
	
	@Test
	public void removeBook() throws BookStoreInformationException {
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setAuthor("Henry");
		newBookInformation.setIsbn("123-45");
		newBookInformation.setNumberOfCopies(1);
		newBookInformation.setVersion(1L);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity(HttpStatus.OK);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(newBookInformation));
		assertTrue(bookStoreInformationController.removeBook(newBookInformation.getIsbn()) instanceof ResponseEntity);

	}

	@Test(expected = BookStoreInformationException.class)
	public void removeBookExceptionTest() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setAuthor("Henry");
		newBookInformation.setIsbn("123-45");
		newBookInformation.setNumberOfCopies(1);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.empty());
		bookStoreInformationController.removeBook(information.getIsbn());

	}


}
