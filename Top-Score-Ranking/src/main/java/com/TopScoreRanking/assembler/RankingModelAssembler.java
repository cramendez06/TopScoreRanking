package com.TopScoreRanking.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.TopScoreRanking.controller.RankingController;
import com.TopScoreRanking.entities.Ranking;

/**
 * Assembler for Ranking RESTful output when searched by player
 * 
 * @author Marc Mendez
 */
@Component
public class RankingModelAssembler implements RepresentationModelAssembler<Ranking, EntityModel<Ranking>> {

	/**
	 * Assembler for ranking score list by player
	 * 
	 * @param rank - Ranking
	 * @return EntityModel<Ranking>
	 */
	@Override
	public EntityModel<Ranking> toModel(Ranking rank) {

		return EntityModel.of(rank, 
				linkTo(methodOn(RankingController.class).getHistory(rank.getPlayer())).withSelfRel(),
				linkTo(methodOn(RankingController.class).searchById(rank.getId())).withRel("record"),
				linkTo(methodOn(RankingController.class).all()).withRel("all"));
	}
}
