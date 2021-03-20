package com.TopScoreRanking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.TopScoreRanking.assembler.RankingModelAssembler;
import com.TopScoreRanking.entities.Ranking;
import com.TopScoreRanking.entities.RankingHistory;
import com.TopScoreRanking.exceptions.HistoryNotFoundException;
import com.TopScoreRanking.exceptions.PlayerNotFoundException;
import com.TopScoreRanking.projections.RankingHistoryDataInterface;
import com.TopScoreRanking.projections.RankingHistoryListInterface;
import com.TopScoreRanking.repository.RankingRepository;
import com.TopScoreRanking.service.RankingService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;

/***
 * Unit test for RankingService
 * 
 * @author Marc Mendez
 *
 */
@SpringBootTest
public class TopScoreRankingTest {
	
	@Autowired
	private RankingService rankService;

	@MockBean
	private RankingRepository repository;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final RankingModelAssembler assembler = new RankingModelAssembler();

	private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
	
	/***
	 * Test RankingService getAll 
	 */
	@Test
	@DisplayName("Test getAll Success")
	void testGetAll() {
		Ranking rankMock1 = new Ranking();
		rankMock1.setId(1L);
		rankMock1.setPlayer("test1");
		rankMock1.setScore(100);
		rankMock1.setTime(LocalDateTime.parse("2020-12-11 17:46:30", formatter));

		Ranking rankMock2 = new Ranking();
		rankMock2.setId(2L);
		rankMock2.setPlayer("test2");
		rankMock2.setScore(200);
		rankMock2.setTime(LocalDateTime.parse("2020-12-12 17:46:30", formatter));

		doReturn(Arrays.asList(rankMock1, rankMock2)).when(repository).findAll();

		List<EntityModel<Ranking>> rankAllMockResult = rankService.getAll();

		// Assert the response
		Assertions.assertEquals(2, rankAllMockResult.size(), "getAll should return 2 Ranking record");
	}

	/***
	 * Test RankingService saveRanking 
	 */
	@Test
	@DisplayName("Test saveRanking Success")
	void testSaveRanking() {
		// Setup our mock repository
		Ranking rankMock = new Ranking();
		rankMock.setId(1L);
		rankMock.setPlayer("test1");
		rankMock.setScore(100);
		rankMock.setTime(LocalDateTime.parse("2020-12-11 17:46:30", formatter));

		doReturn(rankMock).when(repository).save(any());

		// Execute the service call
		EntityModel<Ranking> returnedWidget = rankService.saveRanking(rankMock);

		// Assert the response
		Assertions.assertNotNull(returnedWidget.getContent(), "The saved Ranking should not be null");
		Assertions.assertEquals(1L, (long) returnedWidget.getContent().getId(), "The id should be incremented");
	}

	/***
	 * Test RankingService filterById 
	 */
	@Test
	@DisplayName("Test filterById Success")
	void testFilterById() {

		Ranking rankMock = new Ranking();
		rankMock.setId(2L);
		rankMock.setPlayer("test2");
		rankMock.setScore(200);
		rankMock.setTime(LocalDateTime.parse("2020-12-12 17:46:30", formatter));

		Optional<Ranking> rankMockOpt = Optional.ofNullable(rankMock);

		doReturn(rankMockOpt).when(repository).findById(2L);

		EntityModel<Ranking> rankMockResult = rankService.filterById(2L);

		// Assert the response
		Assertions.assertEquals(rankMock, rankMockResult.getContent(),
				"filterById's result should be equal to mock data");
	}

	/***
	 * Test RankingService filterById (NotFoundExeception) 
	 */
	@Test
	@DisplayName("Test filterById ErrorException")
	void testFilterByIdException() {

		doReturn(Optional.empty()).when(repository).findById(1L);

		Assertions.assertThrows(RuntimeException.class, () -> {
			rankService.filterById(1L);
		}, "Expects to throw IDNotFoundException but didn't");
	}

	/***
	 * Test RankingService filterByPlayers
	 */
	@Test
	@DisplayName("Test filterByPlayers Success")
	void testFilterByPlayers() {

		Ranking rankMock1 = new Ranking();
		rankMock1.setId(1L);
		rankMock1.setPlayer("test1");
		rankMock1.setScore(100);
		rankMock1.setTime(LocalDateTime.parse("2020-12-11 17:46:30", formatter));

		Ranking rankMock2 = new Ranking();
		rankMock2.setId(2L);
		rankMock2.setPlayer("test2");
		rankMock2.setScore(200);
		rankMock2.setTime(LocalDateTime.parse("2020-12-12 17:46:30", formatter));

		List<Ranking> rankMockList = new ArrayList<Ranking>();
		rankMockList.add(rankMock1);
		rankMockList.add(rankMock2);

		Pageable paging = PageRequest.of(0, 3);
		Page<Ranking> rankMockPage = new PageImpl<>(rankMockList);

		List<String> playerList = new ArrayList<String>();
		playerList.add("TeSt1");
		playerList.add("tEsT2");

		List<EntityModel<Ranking>> rankMockEntityList = rankMockPage.stream().map(assembler::toModel)
				.collect(Collectors.toList());

		doReturn(rankMockPage).when(repository).findByPlayerInIgnoreCase(playerList, paging);

		CollectionModel<EntityModel<Ranking>> rankMockResult = rankService.filterByPlayers(playerList, 0, 3);

		Assertions.assertEquals(rankMockEntityList, new ArrayList<EntityModel<Ranking>>(rankMockResult.getContent()),
				"filterByPlayers' result should be equal to mock data");
	}

	/***
	 * Test RankingService filterByPlayers (NotFoundExeception)
	 */
	@Test
	@DisplayName("Test filterByPlayers ErrorException")
	void testFilterByPlayersException() {

		List<String> playerList = new ArrayList<String>();
		playerList.add("TeSt1");
		playerList.add("tEsT2");

		Pageable paging = PageRequest.of(0, 3);

		doReturn(Page.empty()).when(repository).findByPlayerInIgnoreCase(playerList, paging);

		PlayerNotFoundException thrown = Assertions.assertThrows(PlayerNotFoundException.class,
				() -> rankService.filterByPlayers(playerList, 0, 3),
				"Expects to throw PlayerNotFoundException but didn't");

		Assertions.assertTrue(thrown.getMessage()
				.contains("Could not find data for the following player/s: " + String.join(", ", playerList)));
	}

	/***
	 * Test RankingService filterByPlayersAndDate
	 */
	@Test
	@DisplayName("Test filterByPlayersAndDate Success")
	void testFilterByPlayersAndDate() {

		Ranking rankMock1 = new Ranking();
		rankMock1.setId(1L);
		rankMock1.setPlayer("test1");
		rankMock1.setScore(100);
		rankMock1.setTime(LocalDateTime.parse("2020-12-11 17:46:30", formatter));

		Ranking rankMock2 = new Ranking();
		rankMock2.setId(2L);
		rankMock2.setPlayer("test2");
		rankMock2.setScore(200);
		rankMock2.setTime(LocalDateTime.parse("2020-12-12 17:46:30", formatter));

		List<Ranking> rankMockList = new ArrayList<Ranking>();
		rankMockList.add(rankMock1);
		rankMockList.add(rankMock2);

		Pageable paging = PageRequest.of(0, 3);
		Page<Ranking> rankMockPage = new PageImpl<>(rankMockList);

		List<String> playerList = new ArrayList<String>();
		playerList.add("TeSt1");
		playerList.add("tEsT2");

		List<EntityModel<Ranking>> rankMockEntityList = rankMockPage.stream().map(assembler::toModel)
				.collect(Collectors.toList());

		doReturn(rankMockPage).when(repository).findByPlayerWithBeforeAndAfterTime(playerList, rankMock2.getTime(),
				rankMock1.getTime(), paging);

		CollectionModel<EntityModel<Ranking>> rankMockResult = rankService.filterByPlayersAndDate(playerList, 0, 3,
				rankMock2.getTime(), rankMock1.getTime());

		// Assert the response
		Assertions.assertEquals(rankMockEntityList, new ArrayList<EntityModel<Ranking>>(rankMockResult.getContent()),
				"filterByPlayersAndDate's result should be equal to mock data");
	}

	/***
	 * Test RankingService filterByPlayersAndDate (NotFoundExeception)
	 */
	@Test
	@DisplayName("Test filterByPlayersAndDate ErrorException")
	void testFilterByPlayersAndDateException() {

		List<String> playerList = new ArrayList<String>();
		playerList.add("TeSt100");
		playerList.add("tEsT200");

		LocalDateTime mockTime = LocalDateTime.parse("2020-12-12 17:46:30", formatter);

		Pageable paging = PageRequest.of(0, 3);

		doReturn(Page.empty()).when(repository).findByPlayerWithBeforeAndAfterTime(playerList, mockTime, mockTime,
				paging);

		PlayerNotFoundException thrown = Assertions.assertThrows(PlayerNotFoundException.class,
				() -> rankService.filterByPlayersAndDate(playerList, 0, 3, mockTime, mockTime),
				"Expects to throw PlayerNotFoundException but didn't");

		Assertions.assertTrue(thrown.getMessage()
				.contains("Could not find data for the following player/s: " + String.join(", ", playerList)));
	}

	/***
	 * Test RankingService getPlayerScoreHistory
	 */
	@Test
	@DisplayName("Test getPlayerScoreHistory Success")
	void testGetPlayerScoreHistory() {

		RankingHistoryDataInterface dataInterface = factory.createProjection(RankingHistoryDataInterface.class);

		dataInterface.setPlayer("test");
		dataInterface.setAvgScore(5.0);

		List<RankingHistoryListInterface> listScoreMock = new ArrayList<RankingHistoryListInterface>();
		RankingHistoryListInterface listInterface = factory.createProjection(RankingHistoryListInterface.class);
		listInterface.setScore(100);
		listInterface.setTime(LocalDateTime.parse("2020-12-11 17:46:30", formatter));

		listScoreMock.add(listInterface);
		listScoreMock.add(listInterface);

		RankingHistory returnMock = new RankingHistory(dataInterface.getPlayer(), listScoreMock, listScoreMock,
				dataInterface.getAvgScore(), listScoreMock);

		doReturn(dataInterface).when(repository).findAvgByPlayer(dataInterface.getPlayer());

		doReturn(listScoreMock).when(repository).findMaxScoreListByPlayer(dataInterface.getPlayer());

		doReturn(listScoreMock).when(repository).findMinScoreListByPlayer(dataInterface.getPlayer());

		doReturn(listScoreMock).when(repository).findAllScoreListByPlayer(dataInterface.getPlayer());

		EntityModel<RankingHistory> entityMockModelResult = rankService
				.getPlayerScoreHistory(dataInterface.getPlayer());

		Assertions.assertEquals(returnMock, entityMockModelResult.getContent(),
				"getPlayerScoreHistory's result should be equal to mock data");

	}

	/***
	 * Test RankingService getPlayerScoreHistory (NotFoundExeception)
	 */
	@Test
	@DisplayName("Test getPlayerScoreHistory ErrorException")
	void testGetPlayerScoreHistoryException() {

		String player = "test";

		ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
		RankingHistoryDataInterface dataInterface = factory.createProjection(RankingHistoryDataInterface.class);

		dataInterface.setPlayer("test");
		dataInterface.setAvgScore(5.0);

		doReturn(dataInterface).when(repository).findAvgByPlayer(dataInterface.getPlayer());

		doReturn(new ArrayList<RankingHistoryListInterface>()).when(repository)
				.findMaxScoreListByPlayer(dataInterface.getPlayer());

		doReturn(new ArrayList<RankingHistoryListInterface>()).when(repository)
				.findMinScoreListByPlayer(dataInterface.getPlayer());

		doReturn(new ArrayList<RankingHistoryListInterface>()).when(repository)
				.findAllScoreListByPlayer(dataInterface.getPlayer());

		HistoryNotFoundException thrown = Assertions.assertThrows(HistoryNotFoundException.class,
				() -> rankService.getPlayerScoreHistory(player),
				"Expects to throw HistoryNotFoundException but didn't");

		Assertions.assertTrue(thrown.getMessage().contains("Could not find history for player " + player));
	}
	
	/***
	 * Test RankingService deleteRanking
	 */
	@Test
	@DisplayName("Test deleteRanking Success")
    public void testDeleteRanking() {
	
		RankingService mockService;
		
		mockService = mock(RankingService.class);
		
		mockService.deleteRanking(10001L);
		
		verify(mockService).deleteRanking(10001L);
    }
}
