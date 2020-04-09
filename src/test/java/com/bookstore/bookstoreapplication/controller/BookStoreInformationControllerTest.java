package com.bookstore.bookstoreapplication.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
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
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(information,
				HttpStatus.OK);
		when(bookInformationService.addBookInformation(information)).thenReturn(response);
		assertTrue(bookStoreInformationController.addBookInformation(information) instanceof ResponseEntity);

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
		when(bookInformationService.addBookInformation(information)).thenReturn(response);
		assertTrue(bookStoreInformationController.addBookInformation(information) instanceof ResponseEntity);

	}

	@Test
	public void fetchBookBasedOnIsbn() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(4);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(information,
				HttpStatus.OK);
		when(bookInformationService.fetchBookInformation(any())).thenReturn(response);
		assertTrue(bookStoreInformationController.fetchBookBasedOnIsbn("123-23") instanceof ResponseEntity);

	}

	/*
	 * @Test(expected = BookStoreInformationException.class) public void
	 * fetchBookBasedOnIsbnException() throws BookStoreInformationException {
	 * BookStoreInformation information = new BookStoreInformation();
	 * information.setAuthor("Henry"); information.setIsbn("123-45");
	 * information.setTitle("Maths"); information.setNumberOfCopies(4);
	 * 
	 * when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.empty()
	 * ); bookStoreInformationController.fetchBookBasedOnIsbn("123-23");
	 * 
	 * }
	 */

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
		ResponseEntity<List<BookStoreInformation>> response = new ResponseEntity<List<BookStoreInformation>>(
				bookInformationList, HttpStatus.OK);
		when(bookInformationService.fetchBookInformationFromAuthor(any())).thenReturn(response);
		assertTrue(bookStoreInformationController.fetchBookBasedOnAuthor("williams") instanceof ResponseEntity);

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
		bookInformation.setNumberOfCopies(4);

		List<BookStoreInformation> bookInformationList = new ArrayList<BookStoreInformation>();
		bookInformationList.add(information);
		bookInformationList.add(bookInformation);
		ResponseEntity<List<BookStoreInformation>> response = new ResponseEntity<List<BookStoreInformation>>(
				bookInformationList, HttpStatus.OK);
		when(bookInformationService.fetchBookInformationFromTitle(any())).thenReturn(response);
		assertTrue(bookStoreInformationController.fetchBookBasedOnTitle("maths") instanceof ResponseEntity);

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
		when(bookInformationService.fetchMediaCoverage(any())).thenReturn(response);
		assertTrue(bookStoreInformationController.searchMediaCoverage("123-45") instanceof ResponseEntity);

	}

	/*
	 * @Test(expected = BookStoreInformationException.class) public void
	 * searchMediaCoverageTestException() throws BookStoreInformationException {
	 * ResponseEntity<List<String>> response = null;
	 * when(bookInformationService.searchMediaCoverage(any())).thenReturn(response);
	 * bookStoreInformationController.searchMediaCoverage("123-45");
	 * 
	 * }
	 */

	@Test
	public void buyBook() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setAuthor("Henry");
		newBookInformation.setIsbn("123-45");
		newBookInformation.setNumberOfCopies(1);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(newBookInformation,
				HttpStatus.NO_CONTENT);
		when(bookInformationService.purchaseBook(any(), any())).thenReturn(response);
		assertTrue(bookStoreInformationController.buyBook("123", 1) instanceof ResponseEntity);

	}
	/*
	 * @Test(expected = BookStoreInformationException.class) public void
	 * buyBookExceptionTest() throws BookStoreInformationException {
	 * BookStoreInformation information = new BookStoreInformation();
	 * information.setAuthor("Henry"); information.setIsbn("123-45");
	 * BookStoreInformation newBookInformation = new BookStoreInformation();
	 * newBookInformation.setAuthor("Henry"); newBookInformation.setIsbn("123-45");
	 * newBookInformation.setNumberOfCopies(1);
	 * when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.empty()
	 * ); bookStoreInformationController.buyBook("1",information.getIsbn(),1);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 */
}