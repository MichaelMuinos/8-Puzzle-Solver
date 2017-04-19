package presenter;

import algorithm.AStar;
import algorithm.PuzzleState;

public class PuzzleCommandLinePresenter {

    private AStar aStar;

    public PuzzleCommandLinePresenter(AStar aStar) {
        this.aStar = aStar;
    }

    public void generateRandomPuzzleAndSolve() {
        PuzzleState puzzleState = new PuzzleState();
        puzzleState.generateRandomBoard();
        aStar.performAStar(puzzleState);
    }

    public void takeConfigurationAndSolve(String input) {
        PuzzleState puzzleState = new PuzzleState();
        int[][] b = new int[PuzzleState.SIZE][PuzzleState.SIZE];
        int count = 0;
        for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b[i].length; j++) {
                b[i][j] = Character.getNumericValue(input.charAt(count));
                ++count;
            }
        }
        puzzleState.setBoard(b);
        aStar.performAStar(puzzleState);
    }

}
