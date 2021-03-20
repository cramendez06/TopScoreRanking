package com.TopScoreRanking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.TopScoreRanking.entities.Ranking;
import com.TopScoreRanking.projections.RankingHistoryDataInterface;
import com.TopScoreRanking.projections.RankingHistoryListInterface;

/**
 * JPA Repository for Top Score Ranking
 * 
 * @author Marc Mendez
 */
public interface RankingRepository extends JpaRepository<Ranking, Long> {
	
	// select player by player name
	Page<Ranking> findByPlayerInIgnoreCase(List<String> player, Pageable pageable);

	// select player by player name and dateTime
	@Query("SELECT rk FROM Ranking rk WHERE UPPER(rk.player) IN(UPPER(:player)) AND (:onbefore is null OR rk.time <= :onbefore) AND (:onafter is null OR rk.time >= :onafter)")
	Page<Ranking> findByPlayerWithBeforeAndAfterTime(@Param(value = "player") List<String> player,
			@Param(value = "onbefore") LocalDateTime onbefore, @Param(value = "onafter") LocalDateTime onafter,
			Pageable pageable);

	// select player average score by player
	@Query("SELECT rk.player as player, ROUND(avg(rk.score), 2) as avgScore FROM Ranking rk WHERE UPPER(rk.player) = UPPER(:player) Group by rk.player ")
	RankingHistoryDataInterface findAvgByPlayer(@Param(value = "player") String player);

	// select player top score by player
	@Query("SELECT rk.score as score, rk.time as time FROM Ranking rk WHERE UPPER(rk.player) = UPPER(:player) AND rk.score = (SELECT max(rkmx.score) FROM Ranking rkmx WHERE UPPER(rkmx.player) = UPPER(:player)) ")
	List<RankingHistoryListInterface> findMaxScoreListByPlayer(@Param(value = "player") String player);

	// select player low score by player
	@Query("SELECT rk.score as score, rk.time as time FROM Ranking rk WHERE UPPER(rk.player) = UPPER(:player) AND rk.score = (SELECT min(rkmn.score) FROM Ranking rkmn WHERE UPPER(rkmn.player) = UPPER(:player)) ")
	List<RankingHistoryListInterface> findMinScoreListByPlayer(@Param(value = "player") String player);

	// select player all score by player
	@Query("SELECT rk.score as score, rk.time as time FROM Ranking rk WHERE UPPER(rk.player) = UPPER(:player) ")
	List<RankingHistoryListInterface> findAllScoreListByPlayer(@Param(value = "player") String player);
}
