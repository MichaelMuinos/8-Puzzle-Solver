package presenter;

import algorithm.AStar;
import algorithm.PuzzleState;
import com.sun.deploy.util.StringUtils;

public class PuzzleCommandLinePresenter {

    private AStar aStar;

    public PuzzleCommandLinePresenter(AStar aStar) {
        this.aStar = aStar;
    }

    public void generateRandomPuzzleAndSolve() {
        boolean solvable = false;
        PuzzleState puzzleState = new PuzzleState();
        while(!solvable) {
            puzzleState.generateRandomBoard();
            solvable = isSolvable(puzzleState);
        }
        aStar.performAStar(puzzleState);
    }

    public boolean takeConfigurationAndSolve(String input) {
        if(!isValidInput(input)) return false;
        PuzzleState puzzleState = new PuzzleState(input);
        if(!isSolvable(puzzleState)) return false;
        aStar.performAStar(puzzleState);
        return true;
    }

    private boolean isValidInput(String input) {
        String trimInput = StringUtils.trimWhitespace(input);
        if(trimInput.length() != 9) return false;
        for(int i = 0; i < 9; i++) {
            if(trimInput.length() - trimInput.replace(String.valueOf(i), "").length() != 1)
                return false;
        }
        return true;
    }

    private boolean isSolvable(PuzzleState puzzleState) {
        return getNumberOfInversions(puzzleState) % 2 == 0 ? true : false;
    }

    private int getNumberOfInversions(PuzzleState puzzleState) {
        int inversions = 0;
        int[] board = puzzleState.convertToOneDimension();
        for(int i = 0; i < board.length - 1; i++) {
            for(int j = i + 1; j < board.length; j++) {
                if(board[i] > board[j]) ++inversions;
            }
            if(board[i] == 0 && i % 2 == 1) ++inversions;
        }
        return inversions;
    }

}
