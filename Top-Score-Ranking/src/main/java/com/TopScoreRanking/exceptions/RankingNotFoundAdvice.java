package com.TopScoreRanking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception Handler
 * 
 * @author Marc Mendez
 */
@ControllerAdvice
class RankingNotFoundAdvice {

	/**
	 * IDNotFoundException handler
	 * 
	 * @param ex - IDNotFoundException : No results returned exception for ID search
	 * @return String - Error message
	 */
	@ResponseBody
	@ExceptionHandler(IDNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String idNotFoundException(IDNotFoundException ex) {
		return ex.getMessage();
	}

	/**
	 * PlayerNotFoundException Handler
	 * 
	 * @param ex - PlayerNotFoundException : No results returned exception for player search
	 * @return String - Error message
	 */
	@ResponseBody
	@ExceptionHandler(PlayerNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String playerNotFoundHandler(PlayerNotFoundException ex) {
		return ex.getMessage();
	}

	/**
	 * HistoryNotFoundException Handler
	 * 
	 * @param ex - HistoryNotFoundException : No results returned exception for player history
	 * @return String - Error message
	 */
	@ResponseBody
	@ExceptionHandler(HistoryNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String historyNotFoundHandler(HistoryNotFoundException ex) {
		return ex.getMessage();
	}
}
