package com.TopScoreRanking.projections;

/**
 * Interface-based projection for RankingHistory
 * 
 * @author Marc Mendez
 */
public interface RankingHistoryDataInterface {
	String getPlayer();

	double getAvgScore();

	void setPlayer(String player);

	void setAvgScore(double avgScore);
}
