package com.TopScoreRanking.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.TopScoreRanking.assembler.RankingHistoryModelAssembler;
import com.TopScoreRanking.assembler.RankingModelAssembler;
import com.TopScoreRanking.assembler.RankingModelByIdAssembler;
import com.TopScoreRanking.controller.RankingController;
import com.TopScoreRanking.entities.Ranking;
import com.TopScoreRanking.entities.RankingHistory;
import com.TopScoreRanking.exceptions.HistoryNotFoundException;
import com.TopScoreRanking.exceptions.IDNotFoundException;
import com.TopScoreRanking.exceptions.PlayerNotFoundException;
import com.TopScoreRanking.projections.RankingHistoryDataInterface;
import com.TopScoreRanking.projections.RankingHistoryListInterface;
import com.TopScoreRanking.repository.RankingRepository;

/**
 * Business logic handler for Top Score Ranking
 * 
 * @author Marc Mendez
 */
@Service
public class RankingService {

	private final RankingRepository repository;

	private final RankingModelAssembler assembler;
	private final RankingModelByIdAssembler assemblerid;
	private final RankingHistoryModelAssembler assemblerhs;

	/**
	 * Constructor
	 * 
	 * @param repository
	 * @param assembler
	 * @param assemblerid
	 * @param assemblerhs
	 */
	public RankingService(RankingRepository repository, RankingModelAssembler assembler,
			RankingModelByIdAssembler assemblerid, RankingHistoryModelAssembler assemblerhs) {
		this.repository = repository;
		this.assembler = assembler;
		this.assemblerid = assemblerid;
		this.assemblerhs = assemblerhs;
	}

	/**
	 * Get all records from the database
	 * 
	 * @return List<EntityModel<Ranking>> - returns list of player
	 */
	public List<EntityModel<Ranking>> getAll() {
		return repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
	}

	/**
	 * Register new record to database
	 * 
	 * @param newRanking - Ranking : New ranking record to be registered
	 * @return EntityModel<Ranking> - Returns RESTful output
	 */
	public EntityModel<Ranking> saveRanking(Ranking newRanking) {
		return assembler.toModel(repository.save(newRanking));
	}

	/**
	 * Search player's score using ID
	 * 
	 * @param id - Long : Unique ranking ID
	 * @return EntityModel<Ranking> - Returns RESTful output with ranking records searched by ID
	 */
	public EntityModel<Ranking> filterById(Long id) {

		// search player by id, throws Exception if not existing.
		Ranking rank = repository.findById(id).orElseThrow(() -> new IDNotFoundException(id));

		EntityModel<Ranking> entityModel = assemblerid.toModel(rank);

		return entityModel;
	}

	/**
	 * Search player's score list (with Pagination)
	 * 
	 * @param player - List<String> : List of players to search
	 * @param page   - Integer : Current Page no. (Zero base)
	 * @param size   - Integer : Page size
	 * @return CollectionModel<EntityModel<Ranking>> - Returns RESTful output with embedded ranking records searched by ID
	 */
	public CollectionModel<EntityModel<Ranking>> filterByPlayers(List<String> player, int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Ranking> pageDate;

		// Search player by player name (Case Insensitive)
		pageDate = repository.findByPlayerInIgnoreCase(player, paging);

		// Throws exception when no results returned
		if (pageDate.isEmpty()) {
			throw new PlayerNotFoundException(player);
		}

		int totalPage = pageDate.getTotalPages();
		int nextPage = Math.min(page + 1, totalPage - 1);
		int prevPage = Math.max(page - 1, 0);

		List<EntityModel<Ranking>> rank = pageDate.stream().map(assembler::toModel).collect(Collectors.toList());

		// Conditions for RESTful Pagination Links
		if (nextPage == 0 && prevPage == 0) {
			// If results are within a single page
			return CollectionModel.of(rank,
					linkTo(methodOn(RankingController.class).searchByPlayers(player, page, size)).withSelfRel(),
					linkTo(methodOn(RankingController.class).all()).withRel("all"));
		} else if (nextPage == totalPage - 1) {
			// If first page
			return CollectionModel.of(rank,
					linkTo(methodOn(RankingController.class).searchByPlayers(player, page, size)).withSelfRel(),
					linkTo(methodOn(RankingController.class).searchByPlayers(player, prevPage, size))
							.withRel("previous"),
					linkTo(methodOn(RankingController.class).all()).withRel("all"));
		} else if (prevPage == 0) {
			// If last page
			return CollectionModel.of(rank,
					linkTo(methodOn(RankingController.class).searchByPlayers(player, page, size)).withSelfRel(),
					linkTo(methodOn(RankingController.class).searchByPlayers(player, nextPage, size)).withRel("next"),
					linkTo(methodOn(RankingController.class).all()).withRel("all"));
		} else {
			// else, display both next and previous links
			return CollectionModel.of(rank,
					linkTo(methodOn(RankingController.class).searchByPlayers(player, page, size)).withSelfRel(),
					linkTo(methodOn(RankingController.class).searchByPlayers(player, nextPage, size)).withRel("next"),
					linkTo(methodOn(RankingController.class).searchByPlayers(player, prevPage, size))
							.withRel("previous"),
					linkTo(methodOn(RankingController.class).all()).withRel("all"));
		}

	}

	/**
	 * Search player's score list filtered by date (with Pagination)
	 * 
	 * @param player   - List<String> : List of players to search
	 * @param page     - Integer : Current Page no. (Zero base)
	 * @param size     - Integer : Page size
	 * @param onbefore - LocalDateTime : Filter by on or before the given date and time
	 * @param onafter  - LocalDateTime : Filter by on or after the given date and time
	 * @return CollectionModel<EntityModel<Ranking>> - Returns RESTful output with embedded ranking records searched by ID
	 */
	public CollectionModel<EntityModel<Ranking>> filterByPlayersAndDate(List<String> player, int page, int size,
			LocalDateTime onbefore, LocalDateTime onafter) {
		Pageable paging = PageRequest.of(page, size);
		Page<Ranking> pageDate;

		// Search by player name and given date and time
		pageDate = repository.findByPlayerWithBeforeAndAfterTime(player, onbefore, onafter, paging);

		// Throws exception when no results returned
		if (pageDate.isEmpty()) {
			throw new PlayerNotFoundException(player);
		}

		int totalPage = pageDate.getTotalPages();
		int nextPage = Math.min(page + 1, totalPage - 1);
		int prevPage = Math.max(page - 1, 0);

		List<EntityModel<Ranking>> rank = pageDate.stream().map(assembler::toModel).collect(Collectors.toList());

		// Conditions for RESTful Pagination Links
		if (nextPage == 0 && prevPage == 0) {
			// If results are within a single page
			return CollectionModel.of(rank,
					linkTo(methodOn(RankingController.class).searchByPlayers(player, page, size, onbefore, onafter))
							.withSelfRel(),
					linkTo(methodOn(RankingController.class).all()).withRel("all"));
		} else if (nextPage == totalPage - 1) {
			// If last page
			return CollectionModel.of(rank,
					linkTo(methodOn(RankingController.class).searchByPlayers(player, page, size, onbefore, onafter))
							.withSelfRel(),
					linkTo(methodOn(RankingController.class).searchByPlayers(player, prevPage, size, onbefore, onafter))
							.withRel("previous"),
					linkTo(methodOn(RankingController.class).all()).withRel("all"));
		} else if (prevPage == 0) {
			// If first page
			return CollectionModel.of(rank,
					linkTo(methodOn(RankingController.class).searchByPlayers(player, page, size, onbefore, onafter))
							.withSelfRel(),
					linkTo(methodOn(RankingController.class).searchByPlayers(player, nextPage, size, onbefore, onafter))
							.withRel("next"),
					linkTo(methodOn(RankingController.class).all()).withRel("all"));
		} else {
			// else, display both next and previous links
			return CollectionModel.of(rank,
					linkTo(methodOn(RankingController.class).searchByPlayers(player, page, size, onbefore, onafter))
							.withSelfRel(),
					linkTo(methodOn(RankingController.class).searchByPlayers(player, nextPage, size, onbefore, onafter))
							.withRel("next"),
					linkTo(methodOn(RankingController.class).searchByPlayers(player, prevPage, size, onbefore, onafter))
							.withRel("previous"),
					linkTo(methodOn(RankingController.class).all()).withRel("all"));
		}
	}

	/***
	 * Search player's history. Result contains: 
	 * <p>Top score (time and score) which the best ever score of the player.</p>
	 * <p>Low score (time and score) worst score of the player.</p>
	 * <p>Average score value for player</p>
	 * <p>List of all the scores (time and score) of this player.</p>
	 * 
	 * @param player - String : Player name
	 * @return EntityModel<RankingHistory> - Returns RESTful output with player's history searched by player
	 */
	public EntityModel<RankingHistory> getPlayerScoreHistory(String player) {
		// Get player name and player's average score
		RankingHistoryDataInterface playerAndAvgScore = repository.findAvgByPlayer(player);

		// Get player's top score list
		List<RankingHistoryListInterface> topScoreList = repository.findMaxScoreListByPlayer(player);

		// Get player's low score list
		List<RankingHistoryListInterface> lowScoreList = repository.findMinScoreListByPlayer(player);

		// Get player's all score list
		List<RankingHistoryListInterface> allScoreList = repository.findAllScoreListByPlayer(player);

		// Throws exception when no results returned
		if (topScoreList.isEmpty() || lowScoreList.isEmpty() || allScoreList.isEmpty()) {
			throw new HistoryNotFoundException(player);
		}

		// Aggregate all results above into the class-based projection
		RankingHistory rankHistory = new RankingHistory(playerAndAvgScore.getPlayer(), topScoreList, lowScoreList,
				playerAndAvgScore.getAvgScore(), allScoreList);

		EntityModel<RankingHistory> entityModel = assemblerhs.toModel(rankHistory);

		return entityModel;
	}

	/***
	 * Delete a record using player's ID
	 * 
	 * @param id - Long : Unique ranking ID
	 */
	public void deleteRanking(Long id) {
		repository.deleteById(id);
	}
}
