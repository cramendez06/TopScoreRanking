package com.TopScoreRanking.projections;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Interface-based projection for RankingHistory(List)
 * 
 * @author Marc Mendez
 */
public interface RankingHistoryListInterface {
	int getScore();

	@JsonFormat(pattern = "yyyyMMddHHmmss")
	LocalDateTime getTime();

	void setScore(int score);

	void setTime(LocalDateTime time);
}
