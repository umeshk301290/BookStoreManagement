package com.bookstore.bookstoreapplication.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.model.MediaCoverageInformation;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;
import com.bookstore.bookstoreapplication.service.BookStoreInformationService;

@RunWith(MockitoJUnitRunner.class)
public class BookStoreInformationServiceTest {

	@InjectMocks
	BookStoreInformationService bookStoreInformationService;

	@Mock
	BookStoreInformationRepository bookInformationRepository;

	@Mock
	RestTemplate restTemplate;
	
	@Mock
	Environment env;

	@Test
	public void checkForAlreadyPresentBookTest() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		

		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setTitle("maths");
		newBookInformation.setAuthor("Matt");
		newBookInformation.setIsbn("123-46");
		newBookInformation.setPrice(new BigDecimal(123.56));
		newBookInformation.setVersion(1L);
		newBookInformation.setNumberOfCopies(1);	
		when(bookInformationRepository.save(any())).thenReturn(newBookInformation);
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(newBookInformation));
		assertTrue(bookStoreInformationService.checkForAlreadyPresentBookForAdd("1",newBookInformation) instanceof BookStoreInformation);
		

		
}

	@Test(expected=BookStoreInformationException.class)
	public void checkForAlreadyPresentBookExceptionTest() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		
		BookStoreInformation bookInformation = new BookStoreInformation();
		bookInformation.setTitle("maths");
		bookInformation.setAuthor("Matt");
		bookInformation.setIsbn("123-46");
		bookInformation.setPrice(new BigDecimal(124.56));
		bookInformation.setVersion(1L);
		BookStoreInformation newBookInformation = new BookStoreInformation();
		newBookInformation.setTitle("maths");
		newBookInformation.setAuthor("Matt");
		newBookInformation.setIsbn("123-46");
		newBookInformation.setPrice(new BigDecimal(123.56));
		newBookInformation.setVersion(2L);
		
		
		newBookInformation.setNumberOfCopies(1);		
		when(bookInformationRepository.findbyIsbn(any())).thenReturn(Optional.of(newBookInformation));
		bookStoreInformationService.checkForAlreadyPresentBookForAdd("1",bookInformation);
		

		
}
	
		

	@Test
	public void buyBookTest() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(3);	
		information.setVersion(1L);
		when(bookInformationRepository.save(any())).thenReturn(information);
		assertTrue(bookStoreInformationService.checkForAlreadyPresentBookForBuy("1",information,2) instanceof BookStoreInformation);
		
}
	@Test
	public void buyBookTestWithDelete() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(1);	
		information.setVersion(1L);
		assertNull(bookStoreInformationService.checkForAlreadyPresentBookForBuy("1",information,1));
		
}
	
	@Test(expected=BookStoreInformationException.class)
	public void buyBookQuantityTest() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(1);	
		information.setVersion(1L);
		assertNull(bookStoreInformationService.checkForAlreadyPresentBookForBuy("1",information,4));
		
}

	@Test(expected=BookStoreInformationException.class)
	public void buyBookInvalidVersionTest() throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation information = new BookStoreInformation();
		information.setAuthor("Henry");
		information.setIsbn("123-45");
		information.setTitle("Maths");
		information.setNumberOfCopies(1);	
		information.setVersion(2L);
		assertNull(bookStoreInformationService.checkForAlreadyPresentBookForBuy("1",information,4));
		
}


	@Test
	public void checkFormediaCoverage() throws BookStoreInformationException {
	ReflectionTestUtils.setField(bookStoreInformationService, "mediaCoverageUrl", "http://media");
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
	assertTrue(bookStoreInformationService.searchMediaCoverage("123-45") instanceof ResponseEntity);

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
	
	bookStoreInformationService.searchMediaCoverage("123-45");

	}
	
	@Test(expected=Exception.class)
	public void checkFormediaCoverageException() throws BookStoreInformationException {
	ReflectionTestUtils.setField(bookStoreInformationService, "mediaCoverageUrl", "http://media");
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
	bookStoreInformationService.searchMediaCoverage("123-45");

	}

}