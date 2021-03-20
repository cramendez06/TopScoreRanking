package com.TopScoreRanking.exceptions;

import java.util.List;

/**
 * Exception when player record returns no result when search by player
 * 
 * @author Marc Mendez
 */
public class PlayerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5542472927033681919L;

	/**
	 * If player is not existing when searching using player name
	 * 
	 * @param players - player name
	 */
	public PlayerNotFoundException(List<String> players) {
		super("Could not find data for the following player/s: " + String.join(", ", players));
	}
}
