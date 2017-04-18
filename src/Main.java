import models.PuzzleState;
import presenter.AStar;
import ui.PuzzleCommandLine;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        AStar a = new AStar(null);
        PuzzleState puzzleState = new PuzzleState();
        puzzleState.generateRandomBoard();
        a.getSuccessors(puzzleState);
    }

}
