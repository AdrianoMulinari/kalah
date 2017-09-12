package com.coding.challenge.kalah;

import org.hibernate.type.IntegerType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KalahServiceImplTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private KalahServiceImpl kalahService;

    @Test
    public void shouldReturnInitialBoard_when_createNewBoardCall() {
        //When
        Board board = kalahService.createNewBoard();

        //Then
        verify(boardRepository).save(any(Board.class));
        assertThat(board).isNotNull();
        assertThat(board.getBoardRows()).isNotNull().size().isEqualTo(3);
        assertThat(board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits()).isNotNull().size().isEqualTo(KalahConstants.INITIAL_SEED);
        board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().forEach(integer -> assertThat(integer).isEqualTo(KalahConstants.INITIAL_SEED));

        assertThat(board.getBoardRows().get(KalahConstants.STORE_ROW).getPits()).isNotNull().size().isEqualTo(KalahConstants.INITIAL_SEED);
        board.getBoardRows().get(KalahConstants.STORE_ROW).getPits().forEach(integer -> assertThat(integer).isEqualTo(IntegerType.ZERO));

        assertThat(board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits()).isNotNull().size().isEqualTo(KalahConstants.INITIAL_SEED);
        board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().forEach(integer -> assertThat(integer).isEqualTo(KalahConstants.INITIAL_SEED));
    }

    @Test
    public void shouldSeedSixPitsClockWiseIncludingPlayerONEsStore_when_playerOne_choosesFourthPit() {
        //Given
        Board board = createBoard();
        Board seededBoard = createBoard();
        board.setId(1L);
        int fourthPitIndex = 3;

        //When
        when(boardRepository.findOne(board.getId())).thenReturn(seededBoard);
        Board result = kalahService.moveSeed(fourthPitIndex, board.getId());

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getBoardRows()).isNotNull();
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits()).isNotNull();

        for (int i = 0; i < fourthPitIndex; i++ ) {
            assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(i))
                    .isEqualTo(board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(i) + 1);
        }
        assertThat(result.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.INITIAL_INDEX))
                .isEqualTo(board.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.INITIAL_INDEX) + 1);

        for (int i = 0; i < fourthPitIndex-1; i++ ) {
            assertThat(result.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(i))
                    .isEqualTo(board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(i) + 1);
        }

    }

    @Test
    public void shouldSeedSixPitsClockWiseIncludingPlayerTwosStore_when_playerTwo_choosesFourthPit() {
        //Given
        Board board = createBoard();
        board.setId(1L);

        Board seededBoard = createBoard();
        seededBoard.setPlayer(PlayerEnum.TWO);
        int fourthPitIndex = 3;

        //When
        when(boardRepository.findOne(board.getId())).thenReturn(seededBoard);
        Board result = kalahService.moveSeed(fourthPitIndex, board.getId());

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getBoardRows()).isNotNull();
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits()).isNotNull();

        for (int i = fourthPitIndex+1; i <= KalahConstants.LAST_INDEX; i++ ) {
            assertThat(result.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(i))
                    .isEqualTo(board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(i) + 1);
        }
        assertThat(result.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.LAST_INDEX))
                .isEqualTo(board.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.LAST_INDEX) + 1);

        for (int i = KalahConstants.LAST_INDEX; i > fourthPitIndex-1; i-- ) {
            assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(i))
                    .isEqualTo(board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(i) + 1);
        }

    }

    @Test
    public void shouldSeedEightPitsClockWiseExceptPlayerTWOStores_when_palyerOne_choosesFirstPit() {
        //Given
        Board board = createBoard();
        Board seededBoard = createBoard();
        board.setId(1L);
        int firstPitIndex = 0;
        seededBoard.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().set(firstPitIndex, 8);

        //When
        when(boardRepository.findOne(board.getId())).thenReturn(seededBoard);
        Board result = kalahService.moveSeed(firstPitIndex, board.getId());

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getBoardRows()).isNotNull();
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits()).isNotNull();


        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(firstPitIndex)).isEqualTo(IntegerType.ZERO);
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(KalahConstants.LAST_INDEX)).isEqualTo(
                board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(KalahConstants.LAST_INDEX) + 1);

        assertThat(result.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.INITIAL_INDEX))
                .isEqualTo(board.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.INITIAL_INDEX) + 1);

        assertThat(result.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.LAST_INDEX))
                .isEqualTo(board.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.LAST_INDEX));

        for (int i = 0; i <= KalahConstants.LAST_INDEX; i++ ) {
            assertThat(result.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(i))
                    .isEqualTo(board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(i) + 1);
        }
    }

    @Test
    public void shouldCaptureAllPlayerAndAdversaryPitToManacala_when_lastSeedIsDropedInAnOwnedEmptyPit() {

        //Given
        Board board = createBoard();
        Board seededBoard = createBoard();
        board.setId(1L);
        int seccondPitIndex = 1;
        seededBoard.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().set(0, 0);
        seededBoard.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().set(seccondPitIndex, 1);

        //When
        when(boardRepository.findOne(board.getId())).thenReturn(seededBoard);
        Board result = kalahService.moveSeed(seccondPitIndex, board.getId());

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getBoardRows()).isNotNull();
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits()).isNotNull();


        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(seccondPitIndex)).isEqualTo(IntegerType.ZERO);
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(0)).isEqualTo(IntegerType.ZERO);
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(0)).isEqualTo(IntegerType.ZERO);
        assertThat(result.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(0)).isEqualTo(7);
    }


    @Test
    public void shouldNotChangeThePlayer_when_lastSeedGoesIntoPlayersStore() {

        //Given
        Board board = createBoard();
        Board seededBoard = createBoard();
        board.setId(1L);
        int lastPitIndex = 5;

        //When
        when(boardRepository.findOne(board.getId())).thenReturn(seededBoard);
        Board result = kalahService.moveSeed(lastPitIndex, board.getId());

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getBoardRows()).isNotNull();
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits()).isNotNull();

        assertThat(board.getPlayer()).isEqualTo(result.getPlayer());
    }

    @Test
    public void shouldGatherAllAdversarySeedsToHisOwnedStore_when_allPlayerOnePitsAreEmpty() {

        //Given
        Board board = createBoard();
        Board seededBoard = createBoard();
        board.setId(1L);
        int lastPitIndex = 5;

        for(int i = 0; i < KalahConstants.LAST_INDEX; i++) {
            seededBoard.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().set(i, IntegerType.ZERO);
        }
        seededBoard.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().set(lastPitIndex, 1);
        seededBoard.getBoardRows().get(KalahConstants.STORE_ROW).getPits().set(KalahConstants.INITIAL_INDEX, 40);

        //When
        when(boardRepository.findOne(board.getId())).thenReturn(seededBoard);
        Board result = kalahService.moveSeed(lastPitIndex, board.getId());

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getBoardRows()).isNotNull();
        assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits()).isNotNull();

        for(int i = 0; i < KalahConstants.LAST_INDEX; i++) {
            assertThat(result.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(i)).isEqualTo(IntegerType.ZERO);
            assertThat(result.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(i)).isEqualTo(IntegerType.ZERO);
        }
        assertThat(result.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.INITIAL_INDEX)).isEqualTo(47);
        assertThat(result.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(KalahConstants.LAST_INDEX)).isEqualTo(30);
    }

    private Board createBoard() {
        Board board = new Board();
        board.setPlayer(PlayerEnum.ONE);

        List<BoardRow> boardRows = new ArrayList<>();

        boardRows.add(kalahService.createPlayerRow(board));
        boardRows.add(kalahService.createStoreRow(board));
        boardRows.add(kalahService.createPlayerRow(board));

        board.setBoardRows(boardRows);
        return board;
    }
}