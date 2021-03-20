package com.TopScoreRanking.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Arrays;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.TopScoreRanking.controller.RankingController;
import com.TopScoreRanking.entities.RankingHistory;

/**
 * Assembler for RankingHistory RESTful output
 * 
 * @author Marc Mendez
 */
@Component
public class RankingHistoryModelAssembler
		implements RepresentationModelAssembler<RankingHistory, EntityModel<RankingHistory>> {

	/**
	 * Assembler for player history
	 * 
	 * @param rankHistory - Ranking History
	 * @return EntityModel<RankingHistory>
	 */
	@Override
	public EntityModel<RankingHistory> toModel(RankingHistory rankHistory) {

		return EntityModel.of(rankHistory, 
				linkTo(methodOn(RankingController.class).getHistory(rankHistory.getPlayer())).withSelfRel(),
				linkTo(methodOn(RankingController.class).searchByPlayers(Arrays.asList(rankHistory.getPlayer()), 0, 3))
						.withSelfRel(),
				linkTo(methodOn(RankingController.class).all()).withRel("all"));
	}
}
