package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleState {

    private static final int SIZE = 3;
    private static final Random random = new Random();
    private static final List<Integer> boardNumbers = new ArrayList<>();

    private int[][] board = new int[SIZE][SIZE];
    // variables used for the equation f = g + h
    private int f = 0;
    private int g = 0;
    private int h = 0;

    public static final int[][] goal = new int[SIZE][SIZE];

    static {
        // initialize contents of goal and boardNumbers
        int count = 0;
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                goal[i][j] = count;
                boardNumbers.add(count);
                ++count;
            }
        }
    }

    // Used to initialize random board
    public PuzzleState() {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                int index = random.nextInt(boardNumbers.size());
                board[i][j] = boardNumbers.get(index);
                boardNumbers.remove(index);
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public String toString() {
        String boardToString = "";
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                boardToString += String.valueOf(board[i][j]) + " ";
            }
            boardToString += "\n";
        }
        return boardToString;
    }
}
