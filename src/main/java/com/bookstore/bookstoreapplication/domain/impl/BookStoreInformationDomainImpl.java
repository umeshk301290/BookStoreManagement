package com.bookstore.bookstoreapplication.domain.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.bookstore.bookstoreapplication.domain.BookStoreInformationDomain;
import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.model.MediaCoverageInformation;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookStoreInformationDomainImpl implements BookStoreInformationDomain{

	@Autowired
	Environment env;

	@Autowired
	BookStoreInformationRepository bookInformationRepository;
	
	@Autowired
	RestTemplate restTemplate;
	

	@Value("${media.coverage.url}")
	String mediaCoverageUrl;

	/**
	 * @param bookStoreInformation
	 * @param book
	 * @return
	 * @throws BookStoreInformationException
	 */
	
	public ResponseEntity<BookStoreInformation> addBookInformation(BookStoreInformation bookStoreInformation,
			BookStoreInformation book) throws BookStoreInformationException {
		if (Objects.nonNull(bookStoreInformation)) {
			log.info("book is already present in bookstore");
			if (bookStoreInformation.getAuthor().equalsIgnoreCase(book.getAuthor())
					&& bookStoreInformation.getPrice().equals(book.getPrice())
					&& bookStoreInformation.getTitle().equalsIgnoreCase(book.getTitle())) {
				if (Objects.nonNull(book.getNumberOfCopies())) {
					bookStoreInformation
							.setNumberOfCopies(bookStoreInformation.getNumberOfCopies() + book.getNumberOfCopies());
				} else {

					bookStoreInformation.setNumberOfCopies(bookStoreInformation.getNumberOfCopies() + 1);
				}
				log.info("book  information is updated with quantity {} ",bookStoreInformation.getNumberOfCopies());
				bookInformationRepository.save(bookStoreInformation);
			} else {
				throw new BookStoreInformationException(env.getProperty("error.book.incorrect.details.message"),
						env.getProperty("error.book.incorrect.details.code"));
			}
			return ResponseEntity.ok().body(bookStoreInformation);

		}
		else {
			log.info("book is not present before in bookstore");
			if (Objects.isNull(book.getNumberOfCopies())) {
				book.setNumberOfCopies(1);
			}
            
			BookStoreInformation bookInformation = bookInformationRepository.save(book);
			log.info("book  information is added with quantity {} ",bookInformation.getNumberOfCopies());
			return new ResponseEntity<BookStoreInformation>(bookInformation, HttpStatus.CREATED);

		}
	}

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
	@Transactional
	public ResponseEntity<List<String>> searchMediaCoverage(@PathVariable("isbn") String isbn)
			throws BookStoreInformationException {
		HttpEntity entity = null;
		List<String> responseList = null;
		ResponseEntity<List<MediaCoverageInformation>> mediaCoverageList = null;
		BookStoreInformation bookStoreInformation = bookInformationRepository.findbyIsbn(isbn).orElseThrow(
				() -> new BookStoreInformationException(env.getProperty("error.book.isbn.not.found.message"),
						env.getProperty("error.book.isbn.not.found.code")));
		String title = bookStoreInformation.getTitle();
		try {
			log.info("going to hit the url {} for fetching media coverage ", mediaCoverageUrl);
			mediaCoverageList = restTemplate.exchange(mediaCoverageUrl, HttpMethod.GET, entity,
					new ParameterizedTypeReference<List<MediaCoverageInformation>>() {
					});
		} catch (Exception e) {
			log.error("exception occured while connecting the url {} ",mediaCoverageUrl);
			throw new BookStoreInformationException(env.getProperty("error.connection.exception.message"),
					env.getProperty("error.connection.exception.code"));
		}
		if (Objects.nonNull(mediaCoverageList) && Objects.nonNull(mediaCoverageList.getBody())) {
			responseList = mediaCoverageList.getBody().stream()
					.filter(mediaCoverage -> mediaCoverage.getTitle().contains(title)
							|| mediaCoverage.getBody().contains(title))
					.map(bookStorage -> bookStorage.getTitle()).collect(Collectors.toList());
		}
		return ResponseEntity.ok().body(responseList);

	}

	
	/**
	 * @param buyBookInformation
	 * @param quantity
	 * @return
	 * @throws BookStoreInformationException
	 */
	public BookStoreInformation purchaseBook(BookStoreInformation buyBookInformation, Integer quantity) throws BookStoreInformationException {
		// TODO Auto-generated method stub
		if (quantity < 0 || quantity > buyBookInformation.getNumberOfCopies()) {
			log.error("invalid quantity found");
			throw new BookStoreInformationException(env.getProperty("error.book.incorrect.quantity.message") + "We have Only " + buyBookInformation.getNumberOfCopies() + " copies left.",
					env.getProperty("error.book.incorrect.quantity.code"));
		} else {
			buyBookInformation.setNumberOfCopies(buyBookInformation.getNumberOfCopies() - quantity);
			if (buyBookInformation.getNumberOfCopies() == 0) {
				log.info("no copies left.Going to remove the book from bookstore");
				bookInformationRepository.deleteById(buyBookInformation.getId());
				return null;
			} else {
				bookInformationRepository.save(buyBookInformation);
				log.info("updating the book information with quantity {} ",buyBookInformation.getNumberOfCopies());
				return buyBookInformation;

			}

		}

	}

	



}

