package com.bookstore.bookstoreapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author umeshkumar01
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@Setter
@Getter
public class MediaCoverageInformation {
	
	String userId;
	String id;
	String title;
	String body;

}
