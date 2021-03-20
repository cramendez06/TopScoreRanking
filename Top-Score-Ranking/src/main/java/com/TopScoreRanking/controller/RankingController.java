package com.TopScoreRanking.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TopScoreRanking.entities.Ranking;
import com.TopScoreRanking.entities.RankingHistory;
import com.TopScoreRanking.service.RankingService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Controller for converting responses to Json.
 * 
 * @author Marc Louis Mendez
 */
@RestController
public class RankingController {

	@Autowired
	public RankingService rankService;

	/**
	 * Get all records from the database
	 * 
	 * @return CollectionModel<EntityModel<Ranking>> Creates a CollectionModel
	 *         instance with the given links and returns it
	 */
	@GetMapping("/ranking/all")
	public CollectionModel<EntityModel<Ranking>> all() {
		// Gets all record
		List<EntityModel<Ranking>> rank = rankService.getAll();

		return CollectionModel.of(rank, linkTo(methodOn(RankingController.class).all()).withSelfRel());
	}

	/**
	 * Register new record to database
	 * 
	 * @param newRanking - Ranking : New ranking record to be registered (JSON Payload)
	 * @return ResponseEntity<?> - Returns a Response entity 
	 */
	@PostMapping("/ranking/register")
	public ResponseEntity<?> newRanking(@RequestBody Ranking newRanking) {
		// Saves new ranking record
		EntityModel<Ranking> entityModel = rankService.saveRanking(newRanking);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	/**
	 * Search player's score using ID
	 * 
	 * @param id - Long : Unique ranking ID
	 * @return ResponseEntity<?> - Returns a Response entity with the list of scores
	 */
	@GetMapping("/ranking/searchscore")
	public ResponseEntity<?> searchById(@RequestParam(required = true) Long id) {
		
		// Gets record based on unique ID
		EntityModel<Ranking> entityModel = rankService.filterById(id);

		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}

	/**
	 * Search player's score list (with Pagination)
	 * 
	 * @param player - List<String> : List of players to search
	 * @param page   - Integer : Current Page no. (Zero base)
	 * @param size   - Integer : Page size
	 * @return ResponseEntity<?> - Returns a Response entity with the list of scores
	 */
	@GetMapping("/ranking/searchlist")
	public ResponseEntity<?> searchByPlayers(@RequestParam(required = true) List<String> player,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {

		// gets player's score list
		CollectionModel<EntityModel<Ranking>> collectionModel = rankService.filterByPlayers(player, page, size);

		return ResponseEntity
				.created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(collectionModel);
	}

	/**
	 * Search player's score list filtered by date (with Pagination)
	 * 
	 * @param player   - List<String> : List of players to search
	 * @param page     - Integer : Current Page no. (Zero base)
	 * @param size     - Integer : Page size
	 * @param onbefore - LocalDateTime : Filter by on or before the given date and time
	 * @param onafter  - LocalDateTime : Filter by on or after the given date and time
	 * @return ResponseEntity<?> Returns a Response entity with the list of scores
	 */
	@GetMapping("/ranking/timefilter/searchlist")
	public ResponseEntity<?> searchByPlayers(@RequestParam(required = true) List<String> player,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,
			@RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyyMMddHHmmss") LocalDateTime onbefore,
			@RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyyMMddHHmmss") LocalDateTime onafter) {

		// Get player's score list filtered by date
		CollectionModel<EntityModel<Ranking>> collectionModel = rankService.filterByPlayersAndDate(player, page, size,
				onbefore, onafter);

		return ResponseEntity
				.created(collectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(collectionModel);
	}

	/**
	 * Search player's history. Result contains: 
	 * <p>Top score (time and score) which the best ever score of the player.</p>
	 * <p>Low score (time and score) worst score of the player.</p>
	 * <p>Average score value for player</p>
	 * <p>List of all the scores (time and score) of this player.</p>
	 * 
	 * @param player - String : Player's name
	 * @return ResponseEntity<?> - Returns a Response entity with the list of player's score history
	 */
	@GetMapping("/ranking/history")
	public ResponseEntity<?> getHistory(@RequestParam(required = true) String player) {

		// Get player's score history
		EntityModel<RankingHistory> entityModel = rankService.getPlayerScoreHistory(player);

		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}

	/**
	 * Delete a record using player's ID
	 * 
	 * @param id - Long : Ranking record ID to be deleted
	 * @return ResponseEntity<?>
	 */
	@DeleteMapping("/ranking/delete")
	public ResponseEntity<?> deleteRanking(@RequestParam(required = true) Long id) {
		
		// Deletes score
		rankService.deleteRanking(id);

		return ResponseEntity.noContent().build();
	}
}