package com.TopScoreRanking.entities;

import java.util.List;
import java.util.Objects;

import com.TopScoreRanking.projections.RankingHistoryListInterface;

/**
 * Class base projection for player history.
 * 
 * @author Marc Louis Mendez
 */
public class RankingHistory {

	// Player name
	private String player;
	// Top score record list
	private List<RankingHistoryListInterface> topScore;
	// Low score record list
	private List<RankingHistoryListInterface> lowScore;
	// Average score
	private double avgScore;
	// All score record list
	private List<RankingHistoryListInterface> allScore;

	/**
	 * Constructor
	 * 
	 * @param player   - String
	 * @param topScore - List<RankingHistoryListInterface>
	 * @param lowScore - List<RankingHistoryListInterface>
	 * @param avgScore - Double
	 * @param allScore - List<RankingHistoryListInterface>
	 */
	public RankingHistory(String player, List<RankingHistoryListInterface> topScore,
			List<RankingHistoryListInterface> lowScore, double avgScore, List<RankingHistoryListInterface> allScore) {
		this.player = player;
		this.topScore = topScore;
		this.lowScore = lowScore;
		this.avgScore = avgScore;
		this.allScore = allScore;
	}

	// Get player name
	public String getPlayer() {
		return this.player;
	}

	// Get player's list of top scores
	public List<RankingHistoryListInterface> getTopScore() {
		return this.topScore;
	}

	// Get player's list of low scores
	public List<RankingHistoryListInterface> getLowScore() {
		return this.lowScore;
	}

	// Get player's average score
	public double getAvgScore() {
		return this.avgScore;
	}

	// Get all of player's score list
	public List<RankingHistoryListInterface> getAllScore() {
		return this.allScore;
	}

	// Set player name
	public void setPlayer(String player) {
		this.player = player;
	}

	// Set player's list of top scores
	public void setTopScore(List<RankingHistoryListInterface> topScore) {
		this.topScore = topScore;
	}

	// Set player's list of low scores
	public void setLowScore(List<RankingHistoryListInterface> lowScore) {
		this.lowScore = lowScore;
	}

	// Set player's average score
	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}

	// Set all of player's score list
	public void setAllScore(List<RankingHistoryListInterface> allScore) {
		this.allScore = allScore;
	}

	/**
	 * Override for custom equals method. Checks if the passed object is equal to
	 * the instance of RankingHistory
	 * 
	 * @param object
	 * @return boolean - if object is equal to the instance of RankingHistory,
	 *         return true, Else false
	 */
	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof RankingHistory))
			return false;
		RankingHistory rank = (RankingHistory) o;
		return Objects.equals(this.player, rank.player) && Objects.equals(this.topScore, rank.topScore)
				&& Objects.equals(this.lowScore, rank.lowScore) && Objects.equals(this.avgScore, rank.avgScore)
				&& Objects.equals(this.allScore, rank.allScore);
	}

	/**
	 * Override for custom hashCode method. Returns a hash code value.
	 * 
	 * @return integer
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.player, this.topScore, this.lowScore, this.avgScore, this.allScore);
	}

	/**
	 * Override for custom toString method. Returns a string representation of the
	 * object.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return "Rank{" + "player=" + this.player + ", topScore='" + this.topScore + '\'' + ", lowScore='"
				+ this.lowScore + '\'' + ", avgScore='" + this.avgScore + '\'' + ", allScore='" + this.allScore + '\''
				+ '}';
	}
}