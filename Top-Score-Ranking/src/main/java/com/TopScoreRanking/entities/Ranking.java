package com.TopScoreRanking.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity Ranking
 * 
 * @author Marc Mendez
 */
@Entity
public class Ranking {

	// ID (Primary key)
	private @Id @GeneratedValue Long id;

	// Player's name
	private String player;

	// Player's score
	private int score;

	// Date
	@JsonFormat(pattern = "yyyyMMddHHmmss")
	private LocalDateTime time;

	/**
	 * Constructor
	 */
	public Ranking() {
	}

	/**
	 * Constructor
	 * 
	 * @param player - String
	 * @param score  - Integer
	 * @param time   - LocalDateTime
	 */
	public Ranking(String player, int score, LocalDateTime time) {
		this.player = player;
		this.score = score;
		this.time = time;
	}

	// Get player ID
	public Long getId() {
		return this.id;
	}

	// Get player name
	public String getPlayer() {
		return this.player;
	}

	// Get player score
	public Integer getScore() {
		return this.score;
	}

	// Get date and time
	public LocalDateTime getTime() {
		return this.time;
	}

	// Set player ID
	public void setId(Long id) {
		this.id = id;
	}

	// Set player name
	public void setPlayer(String player) {
		this.player = player;
	}

	// Set player score
	public void setScore(Integer score) {
		this.score = score;
	}

	// Set date and time
	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	/**
	 * Override for custom equals method. Checks if the passed object is equal to
	 * the instance of Ranking
	 * 
	 * @param object
	 * @return boolean - if object is equal to the instance of Ranking, return true,
	 *         Else false
	 */
	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Ranking))
			return false;
		Ranking rank = (Ranking) o;
		return Objects.equals(this.id, rank.id) && Objects.equals(this.player, rank.player)
				&& Objects.equals(this.score, rank.score) && Objects.equals(this.time, rank.time);
	}

	/**
	 * Override for custom hashCode method. Returns a hash code value.
	 * 
	 * @return integer
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.player, this.score, this.time);
	}

	/**
	 * Override for custom toString method. Returns a string representation of the
	 * object.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return "Rank{" + "id=" + this.id + ", player='" + this.player + '\'' + ", score='" + this.score + '\''
				+ ", time='" + this.time + '\'' + '}';
	}
}
