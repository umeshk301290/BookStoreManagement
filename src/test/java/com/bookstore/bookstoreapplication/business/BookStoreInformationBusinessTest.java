package com.bookstore.bookstoreapplication.business;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.bookstore.bookstoreapplication.buisness.BookStoreInformationBusiness;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.model.MediaCoverageInformation;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookStoreInformationBusinessTest {

	@InjectMocks
	BookStoreInformationBusiness bookStoreInformationBusiness;

	@Mock
	Environment env;

	@Mock
	BookStoreInformationRepository bookInformationRepository;

	@Mock
	RestTemplate restTemplate;

	@Test
	public void addBookTest() throws BookStoreInformationException {
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setTitle("maths");
		newBookInformation.setAuthor("Matt");
		newBookInformation.setIsbn("123-46");
		newBookInformation.setPrice(new BigDecimal(123.56));
		newBookInformation.setNumberOfCopies(1);
		BookStoreInformation book = new BookStoreInformation();
		book.setTitle("maths");
		book.setAuthor("Matt");
		book.setIsbn("123-46");
		book.setPrice(new BigDecimal(123.56));
		book.setNumberOfCopies(1);
		ResponseEntity<BookStoreInformation> response = new ResponseEntity<BookStoreInformation>(newBookInformation,
				HttpStatus.OK);
		when(bookInformationRepository.save(any())).thenReturn(newBookInformation);
		assertTrue(bookStoreInformationBusiness.addBookInformation(newBookInformation,
				book) instanceof ResponseEntity);

	}
	
	@Test(expected=BookStoreInformationException.class)
	public void addBookExceptionTest() throws BookStoreInformationException {
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setTitle("maths");
		newBookInformation.setAuthor("Matt");
		newBookInformation.setIsbn("123-46");
		newBookInformation.setPrice(new BigDecimal(123.56));
		newBookInformation.setNumberOfCopies(1);
		BookStoreInformation book = new BookStoreInformation();
		book.setTitle("science");
		book.setAuthor("Matt");
		book.setIsbn("123-46");
		book.setPrice(new BigDecimal(123.56));
		book.setNumberOfCopies(1);
		bookStoreInformationBusiness.addBookInformation(newBookInformation,
				book);

	}
	
	@Test
	public void searchmediaCoverage() throws BookStoreInformationException {
		
		ReflectionTestUtils.setField(bookStoreInformationBusiness, "mediaCoverageUrl", "http://media");
		MediaCoverageInformation information = new MediaCoverageInformation();
		information.setBody("maths");
		information.setId("1");
		information.setTitle("maths");
		information.setUserId("2");
		BookStoreInformation bookStoreInfomation = new BookStoreInformation();
		bookStoreInfomation.setTitle("maths");
		
		List<MediaCoverageInformation> mediaList = Arrays.asList(information);
		ResponseEntity<List<MediaCoverageInformation>> mediaCoverageResponseList = new ResponseEntity<List<MediaCoverageInformation>>(mediaList,HttpStatus.OK);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(bookStoreInfomation));
		
		Mockito.when(restTemplate.exchange(
	            Matchers.eq("http://media"),
	            Matchers.eq(HttpMethod.GET),
	            Matchers.<HttpEntity>any(),
	            Matchers.<ParameterizedTypeReference<List<MediaCoverageInformation>>>any())
	        ).thenReturn(mediaCoverageResponseList);
		assertTrue(bookStoreInformationBusiness.searchMediaCoverage("123-45") instanceof ResponseEntity);
		
		
		
		
	}
	@Test(expected=BookStoreInformationException.class)
	public void checkFormediaCoverageNotFound() throws BookStoreInformationException {
	MediaCoverageInformation information = new MediaCoverageInformation();
	information.setBody("maths");
	information.setId("1");
	information.setTitle("maths");
	information.setUserId("2");
	BookStoreInformation bookStoreInfomation = new BookStoreInformation();
	bookStoreInfomation.setTitle("maths");
	
	when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.empty());
	
	bookStoreInformationBusiness.searchMediaCoverage("123-45");

	}
	
	@Test(expected=Exception.class)
	public void searchmediaCoverageException() throws BookStoreInformationException {
		
		ReflectionTestUtils.setField(bookStoreInformationBusiness, "mediaCoverageUrl", "http://media");
		MediaCoverageInformation information = new MediaCoverageInformation();
		information.setBody("maths");
		information.setId("1");
		information.setTitle("maths");
		information.setUserId("2");
		BookStoreInformation bookStoreInfomation = new BookStoreInformation();
		bookStoreInfomation.setTitle("maths");
		
		List<MediaCoverageInformation> mediaList = Arrays.asList(information);
		ResponseEntity<List<MediaCoverageInformation>> mediaCoverageResponseList = new ResponseEntity<List<MediaCoverageInformation>>(mediaList,HttpStatus.OK);
		
		Mockito.when(restTemplate.exchange(
	            Matchers.eq("http://media"),
	            Matchers.eq(HttpMethod.GET),
	            Matchers.<HttpEntity>any(),
	            Matchers.<ParameterizedTypeReference<List<MediaCoverageInformation>>>any())
	        ).thenThrow(new Exception());
		bookStoreInformationBusiness.searchMediaCoverage("123-45");
		
		
		
		
	}
	

	@Test
	public void buyBookTest() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(3);	
		when(bookInformationRepository.save(any())).thenReturn(information);
		assertTrue(bookStoreInformationBusiness.purchaseBook(information,2) instanceof BookStoreInformation);
		
}
	@Test
	public void buyBookTestWithDelete() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(1);	
		assertNull(bookStoreInformationBusiness.purchaseBook(information,1));
		
}

	@Test(expected= BookStoreInformationException.class)
	public void buyBookTestWithInsufficientQuantity() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(1);	
		bookStoreInformationBusiness.purchaseBook(information,10);
		
}

}
