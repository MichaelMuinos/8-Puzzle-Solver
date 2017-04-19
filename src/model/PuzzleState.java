package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleState {

    public static final int SIZE = 3;

    private static final Random random = new Random();
    private static final List<Integer> boardNumbers = new ArrayList<>();

    private int[][] board;
    private int f = 0;
    private int g = 0;
    private int h = 0;

    static {
        // initialize contents of boardNumbers
        for(int i = 0; i < 9; i++) boardNumbers.add(i);
    }

    public PuzzleState() {
        board = new int[SIZE][SIZE];
    }

    // Used to initialize random board
    public void generateRandomBoard() {
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

    public void setBoard(int[][] board) {
        this.board = board;
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
    public boolean equals(Object obj) {
        PuzzleState otherPuzzleState = (PuzzleState) obj;
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                if(this.board[i][j] != otherPuzzleState.board[i][j])
                    return false;
            }
        }
        return true;
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
