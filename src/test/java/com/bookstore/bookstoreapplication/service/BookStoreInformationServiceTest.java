package com.bookstore.bookstoreapplication.service;

import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.bookstore.bookstoreapplication.buisness.BookStoreInformationBusiness;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;
import com.bookstore.bookstoreapplication.service.impl.BookStoreInformationServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class BookStoreInformationServiceTest {

	@InjectMocks
	BookStoreInformationServiceImpl bookStoreInformationService;
	
	@Mock
	BookStoreInformationBusiness bookStoreInformationBusiness;

	@Mock
	BookStoreInformationRepository bookInformationRepository;

	
	@Mock
	Environment env;

	@Test
	public void addBook() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		

		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setTitle("maths");
		newBookInformation.setAuthor("Matt");
		newBookInformation.setIsbn("123-46");
		newBookInformation.setPrice(new BigDecimal(123.56));
		newBookInformation.setNumberOfCopies(1);	
		BookStoreInformation book = new BookStoreInformation();
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(newBookInformation,
				HttpStatus.OK);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(newBookInformation));
		when(bookStoreInformationBusiness.addBookInformation(any(),any())).thenReturn(response);

		assertTrue(bookStoreInformationService.addBookInformation(newBookInformation) instanceof ResponseEntity);
		

		
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
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(information));
		assertTrue(bookStoreInformationService.fetchBookInformation("123-23") instanceof ResponseEntity);

	}

	@Test(expected = BookStoreInformationException.class)
	public void fetchBookBasedOnIsbnException() throws BookStoreInformationException {
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(4);

		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.empty());
		bookStoreInformationService.fetchBookInformation("123-23");

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
		ResponseEntity<List<BookStoreInformation>> response = new ResponseEntity<List<BookStoreInformation>>(bookInformationList,
				HttpStatus.OK);
		when(bookInformationRepository.findbyAuthor(any())).thenReturn(bookInformationList);
		assertTrue(bookStoreInformationService.fetchBookInformationFromAuthor("williams") instanceof ResponseEntity);

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
		ResponseEntity<List<BookStoreInformation>> response = new ResponseEntity<List<BookStoreInformation>>(bookInformationList,
				HttpStatus.OK);
		when(bookInformationRepository.findbyTitle(any())).thenReturn(bookInformationList);
		assertTrue(bookStoreInformationService.fetchBookInformationFromTitle("williams") instanceof ResponseEntity);

	}
	
	@Test
	public void checkFormediaCoverage() throws BookStoreInformationException {
		List<String> mediaList = Arrays.asList("maths","science");
	
	ResponseEntity<List<String>> mediaCoverageResponseList = new ResponseEntity<List<String>>(mediaList,HttpStatus.OK);
	when(bookStoreInformationBusiness.searchMediaCoverage(any())).thenReturn(mediaCoverageResponseList);
	assertTrue(bookStoreInformationService.searchMediaCoverage("123-45") instanceof ResponseEntity);

	}
	
	@Test(expected=BookStoreInformationException.class)
	public void checkFormediaCoverageNotFound() throws BookStoreInformationException {
		List<String> mediaList = null;
		ResponseEntity<List<String>> mediaCoverageResponseList = new ResponseEntity<List<String>>(mediaList,HttpStatus.OK);
		when(bookStoreInformationBusiness.searchMediaCoverage(any())).thenReturn(null);
		assertTrue(bookStoreInformationService.searchMediaCoverage("123-45") instanceof ResponseEntity);
	}
	
	@Test
	public void buyBookTest() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(3);	
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(information));
		when(bookStoreInformationBusiness.purchaseBook(any(),
				any())).thenReturn(information);
		assertTrue(bookStoreInformationService.purchaseBook("1",2) instanceof ResponseEntity);
		
}
	
	@Test(expected=BookStoreInformationException.class)
	public void buyBookQuantityTest() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(3);	
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.empty());
		bookStoreInformationService.purchaseBook("1",2);
		
}

	
	
}