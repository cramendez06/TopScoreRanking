package com.TopScoreRanking.exceptions;

/**
 * Exception when player history returns no result
 * 
 * @author Marc Mendez
 */
public class HistoryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5090276364190117616L;

	/**
	 * If player is not existing when searching score history
	 * 
	 * @param player - player name
	 */
	public HistoryNotFoundException(String player) {
		super("Could not find history for player " + player);
	}
}
