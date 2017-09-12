package com.coding.challenge.kalah;

import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KalahServiceImpl implements KalahService {

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public Board createNewBoard() {
        Board board = new Board();
        board.setPlayer(PlayerEnum.ONE);

        List<BoardRow> boardRows = new ArrayList<>();

        boardRows.add(createPlayerRow(board));
        boardRows.add(createStoreRow(board));
        boardRows.add(createPlayerRow(board));

        board.setBoardRows(boardRows);
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board moveSeed(int pit,  long boardId) {
        Board board = boardRepository.findOne(boardId);

        if(PlayerEnum.ONE.equals(board.getPlayer())) {
            rotateBack(board, KalahConstants.PLAYER_ONE_ROW, pit - 1, board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(pit));
            board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().set(pit, KalahConstants.INITIAL_INDEX);
        }
        else if(PlayerEnum.TWO.equals(board.getPlayer())) {
            rotateForward(board, KalahConstants.PLAYER_TWO_ROW, pit + 1, board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(pit));
            board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().set(pit, KalahConstants.INITIAL_INDEX);
        }

        if (isOutOfStones(board.getBoardRows().get(getPlayerRow(board.getPlayer())))) {
            gatherAdversaryPits(board);
        }

        boardRepository.save(board);
        return board;
    }

    private Board rotateForward(Board board, int row, int pit, int seed) {
        for (int j = pit; j < board.getBoardRows().get(row).getPits().size() && seed > IntegerType.ZERO ; j++) {
            seed = dropSeed(board, row, j, seed);
        }

        if (seed > IntegerType.ZERO) {
            if (PlayerEnum.TWO.equals(board.getPlayer())) {
                seed = dropSeed(board, KalahConstants.STORE_ROW, KalahConstants.LAST_INDEX, seed);
            }
            rotateBack(board, KalahConstants.PLAYER_ONE_ROW, KalahConstants.LAST_INDEX, seed);
        }

        return board;
    }

    private Board rotateBack(Board board, int row, int pit, int seed) {
        for (int j = pit; j >= KalahConstants.INITIAL_INDEX && seed > IntegerType.ZERO; j--) {
            seed = dropSeed(board, row, j, seed);
        }

        if (seed > IntegerType.ZERO) {
            if (PlayerEnum.ONE.equals(board.getPlayer())) {
                seed = dropSeed(board, KalahConstants.STORE_ROW, KalahConstants.INITIAL_INDEX, seed);
            }
            rotateForward(board, KalahConstants.PLAYER_TWO_ROW, KalahConstants.INITIAL_INDEX, seed);
        }
        return board;
    }

    private int dropSeed(Board board, int row, int pit, int seed) {
        seed--;
        int store = getPlayerStore(board.getPlayer());

        if (seed == IntegerType.ZERO && row != KalahConstants.STORE_ROW) {

            if (board.getBoardRows().get(row).getPits().get(pit) == IntegerType.ZERO && getPlayerRow(board.getPlayer()) == row) {
                board.getBoardRows().get(KalahConstants.STORE_ROW).getPits().set(store,
                        board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().get(pit) +
                        board.getBoardRows().get(KalahConstants.STORE_ROW).getPits().get(store) +
                        board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().get(pit) + 1
                );

                board.getBoardRows().get(KalahConstants.PLAYER_ONE_ROW).getPits().set(pit, KalahConstants.INITIAL_INDEX);
                board.getBoardRows().get(KalahConstants.PLAYER_TWO_ROW).getPits().set(pit, KalahConstants.INITIAL_INDEX);
                return seed;
            }
            board.setPlayer(changePlayer(board.getPlayer()));

        }
        board.getBoardRows().get(row).getPits().set(pit, board.getBoardRows().get(row).getPits().get(pit) + 1);

        return seed;
    }

    private PlayerEnum changePlayer(PlayerEnum playerEnum) {
        if (PlayerEnum.ONE.equals(playerEnum)) {
            return PlayerEnum.TWO;
        }
        return PlayerEnum.ONE;
    }

    private int getPlayerStore(PlayerEnum playerEnum) {
        if (PlayerEnum.ONE.equals(playerEnum)) {
            return KalahConstants.INITIAL_INDEX;
        }
        else {
            return KalahConstants.LAST_INDEX;
        }
    }

    private int getPlayerRow(PlayerEnum playerEnum) {
        if (PlayerEnum.ONE.equals(playerEnum)) {
            return KalahConstants.PLAYER_ONE_ROW;
        }
        else {
            return KalahConstants.PLAYER_TWO_ROW;
        }
    }

    private int getAdversaryRow(PlayerEnum playerEnum) {
        if (PlayerEnum.ONE.equals(playerEnum)) {
            return KalahConstants.PLAYER_TWO_ROW;
        }
        else {
            return KalahConstants.PLAYER_ONE_ROW;
        }
    }

    private int getAdversaryStore(PlayerEnum playerEnum) {
        if (PlayerEnum.ONE.equals(playerEnum)) {
            return KalahConstants.LAST_INDEX;
        }
        else {
            return KalahConstants.INITIAL_INDEX;
        }
    }

    private boolean isOutOfStones(BoardRow boardRow) {
        for (int i = 0; i < boardRow.getPits().size() ; i++) {
            if (boardRow.getPits().get(i) != 0) {
                return false;
            }
        }
        return true;
    }

    private void gatherAdversaryPits(Board board) {
        BoardRow adversaryRow = board.getBoardRows().get(getAdversaryRow(board.getPlayer()));
        BoardRow storeRow = board.getBoardRows().get(KalahConstants.STORE_ROW);
        int adversaryStore = getAdversaryStore(adversaryRow.getBoard().getPlayer());

        for (int i = 0; i < adversaryRow.getPits().size(); i++) {
            storeRow.getPits().set(adversaryStore, storeRow.getPits().get(adversaryStore) + adversaryRow.getPits().get(i));
            adversaryRow.getPits().set(i, IntegerType.ZERO);
        }
    }
}
