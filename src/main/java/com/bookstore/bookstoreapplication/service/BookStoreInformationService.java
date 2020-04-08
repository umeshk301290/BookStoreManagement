package com.bookstore.bookstoreapplication.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.bookstore.bookstoreapplication.entity.BookStoreInformation;
import com.bookstore.bookstoreapplication.exception.BookStoreInformationException;
import com.bookstore.bookstoreapplication.model.MediaCoverageInformation;
import com.bookstore.bookstoreapplication.repository.BookStoreInformationRepository;

/**
 * @author umeshkumar01
 *
 */
@Service
public class BookStoreInformationService {

	@Autowired
	BookStoreInformationRepository bookInformationRepository;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Environment env;

	@Value("${media.coverage.url}")
	String mediaCoverageUrl;

	/**
	 * @param eTagHeader
	 * @param book
	 * @return
	 * @throws BookStoreInformationException
	 */
	public BookStoreInformation checkForAlreadyPresentBookForAdd(String eTagHeader, BookStoreInformation book)
			throws BookStoreInformationException {
		// TODO Auto-generated method stub
		BookStoreInformation bookStoreInformation = bookInformationRepository.findbyIsbn(book.getIsbn()).orElse(null);

		if (Objects.nonNull(bookStoreInformation)) {
			if (Objects.isNull(eTagHeader) || !(eTagHeader.equals(String.valueOf(bookStoreInformation.getVersion())))) {
				throw new BookStoreInformationException(env.getProperty("error.duplicate.book.incorrect.etag.message"),
						env.getProperty("error.book.incorrect.etag.code"));
			}
			if (bookStoreInformation.getAuthor().equalsIgnoreCase(book.getAuthor())
					&& bookStoreInformation.getPrice().equals(book.getPrice())
					&& bookStoreInformation.getTitle().equalsIgnoreCase(book.getTitle())) {
				if (Objects.nonNull(book.getNumberOfCopies())) {
					bookStoreInformation
							.setNumberOfCopies(bookStoreInformation.getNumberOfCopies() + book.getNumberOfCopies());
				} else {

					bookStoreInformation.setNumberOfCopies(bookStoreInformation.getNumberOfCopies() + 1);
				}
				bookInformationRepository.save(bookStoreInformation);
			} else {
				throw new BookStoreInformationException(env.getProperty("error.book.incorrect.details.message"),
						env.getProperty("error.book.incorrect.details.code"));
			}
		}
		return bookStoreInformation;
	}

	/**
	 * @param eTagHeader
	 * @param bookStoreInformation
	 * @param quantity
	 * @return
	 * @throws BookStoreInformationException
	 */
	public BookStoreInformation checkForAlreadyPresentBookForBuy(String eTagHeader,
			BookStoreInformation bookStoreInformation, Integer quantity) throws BookStoreInformationException {
		// TODO Auto-generated method stub
		if (!(eTagHeader.equals(String.valueOf(bookStoreInformation.getVersion())))) {
			throw new BookStoreInformationException(env.getProperty("error.book.incorrect.etag.message"),
					env.getProperty("error.book.incorrect.etag.code"));
		}
		if (quantity < 0 || quantity > bookStoreInformation.getNumberOfCopies()) {
			throw new BookStoreInformationException(env.getProperty("error.book.incorrect.quantity.message") + "We have Only " + bookStoreInformation.getNumberOfCopies() + " copies left.",
					env.getProperty("error.book.incorrect.quantity.code"));
		} else {
			bookStoreInformation.setNumberOfCopies(bookStoreInformation.getNumberOfCopies() - quantity);

			if (bookStoreInformation.getNumberOfCopies() == 0) {
				bookInformationRepository.deleteById(bookStoreInformation.getId());
				return null;
			} else {
				bookInformationRepository.save(bookStoreInformation);
				return bookStoreInformation;

			}

		}
	}

	/**
	 * @param isbn
	 * @return
	 * @throws BookStoreInformationException
	 */
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
			mediaCoverageList = restTemplate.exchange(mediaCoverageUrl, HttpMethod.GET, entity,
					new ParameterizedTypeReference<List<MediaCoverageInformation>>() {
					});
		} catch (Exception e) {
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

}
