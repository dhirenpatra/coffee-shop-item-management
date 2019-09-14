
package com.codebydhiren.coffeeshopitemmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.Instant;

public class CustomException extends HttpStatusCodeException {

	private Instant dateOfOccurance;

	protected CustomException(HttpStatus statusCode) {
		super(statusCode);
	}

	public CustomException(HttpStatus statusCode, String statusText) {
		super(statusCode, statusText);
	}

	public Instant getDateOfOccurance() {
		return dateOfOccurance;
	}

	public void setDateOfOccurance(Instant dateOfOccurance) {
		this.dateOfOccurance = dateOfOccurance;
	}
}
