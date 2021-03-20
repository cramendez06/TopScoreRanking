package com.TopScoreRanking.exceptions;

/**
 * Exception when player record returns no result when search by ID
 * 
 * @author Marc Mendez
 */
public class IDNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7895251932085956179L;

	/**
	 * If player is not existing when searching using id
	 * 
	 * @param id - player id
	 */
	public IDNotFoundException(Long id) {
		super("Could not find the following score ID: " + id);
	}
}
