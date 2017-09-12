package com.coding.challenge.kalah;

import java.util.ArrayList;
import java.util.List;

public interface KalahService {

    Board createNewBoard();
    Board moveSeed(int spit,  long boardId);

    default BoardRow createPlayerRow(Board board) {

        List<Integer> pits = new ArrayList<>(KalahConstants.INITIAL_INDEX);
        for(int i = KalahConstants.INITIAL_INDEX; i < KalahConstants.INITIAL_SEED; i++) {
            pits.add(KalahConstants.INITIAL_SEED);
        }
        BoardRow playerRow = new BoardRow();
        playerRow.setBoard(board);
        playerRow.setPits(pits);

        return playerRow;
    }

    default BoardRow createStoreRow(Board board) {
        List<Integer> rows = new ArrayList<>(KalahConstants.INITIAL_INDEX);

        for(int i = KalahConstants.INITIAL_INDEX; i < KalahConstants.INITIAL_SEED; i++) {
            rows.add(KalahConstants.INITIAL_INDEX);
        }

        BoardRow storeRow = new BoardRow();
        storeRow.setBoard(board);
        storeRow.setPits(rows);
        return storeRow;
    }
}
