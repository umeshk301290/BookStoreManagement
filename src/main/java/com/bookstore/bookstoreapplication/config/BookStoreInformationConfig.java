package com.bookstore.bookstoreapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author umeshkumar01
 *
 */
@Configuration
public class BookStoreInformationConfig {

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
