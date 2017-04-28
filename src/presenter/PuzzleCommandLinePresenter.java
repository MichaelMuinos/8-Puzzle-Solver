package presenter;

import algorithm.AStar;
import algorithm.PuzzleState;
import data.Stat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PuzzleCommandLinePresenter {

    private static final DecimalFormat df = new DecimalFormat("#.#######");

    private AStar aStar;
    private Map<Integer,ArrayList<Stat>> testCaseInfoHeuristicOne;
    private Map<Integer,ArrayList<Stat>> testCaseInfoHeuristicTwo;

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

    public boolean takeConfigurationAndSolve(int[][] board) {
        if(!isValidInput(board)) return false;
        PuzzleState puzzleState = new PuzzleState(board);
        if(!isSolvable(puzzleState)) return false;
        aStar.performAStar(puzzleState);
        return true;
    }

    public int[][] generateBoard(int[] board) {
        int[][] matrix = new int[PuzzleState.SIZE][PuzzleState.SIZE];
        int count = 0;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                matrix[i][j] = board[count++];
            }
        }
        return matrix;
    }

    public void processFile(String file) {
        // instantiate our hashmaps
        testCaseInfoHeuristicOne = new HashMap<>();
        testCaseInfoHeuristicTwo = new HashMap<>();
        // attempt to read the contents of the file
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                PuzzleState puzzleState = new PuzzleState(line);
                if(isSolvable(puzzleState)) {
                    Stat statOne = aStar.performAStar(puzzleState, AStar.HEURISTIC_ONE);
                    Stat statTwo = aStar.performAStar(puzzleState, AStar.HEURISTIC_TWO);
                    addToMaps(statOne, statTwo);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printTestCaseDetails(testCaseInfoHeuristicOne, AStar.HEURISTIC_ONE);
        printTestCaseDetails(testCaseInfoHeuristicTwo, AStar.HEURISTIC_TWO);
    }

    private void printTestCaseDetails(Map<Integer,ArrayList<Stat>> map, int heuristic) {
        if(heuristic == AStar.HEURISTIC_ONE) System.out.println("\n----- HEURISTIC ONE DETAILS -----");
        else System.out.println("\n----- HEURISTIC TWO DETAILS -----");
        for(Map.Entry<Integer,ArrayList<Stat>> entry : map.entrySet()) {
            double avgCompletionTime = 0;
            int avgSearchCost = 0;
            int avgNodesGenerated = 0;
            System.out.print("Depth: " + entry.getKey());
            System.out.print("\tNumber of cases: " + entry.getValue().size());
            for(Stat stat : entry.getValue()) {
                avgCompletionTime += stat.getCompletionTime();
                avgSearchCost += stat.getSearchCost();
                avgNodesGenerated += stat.getNodesGenerated();
            }
            System.out.print("\tAvg time: " + df.format(avgCompletionTime / entry.getValue().size()));
            System.out.print("\tAvg nodes generated: " + avgNodesGenerated / entry.getValue().size());
            System.out.print("\tAvg search cost: " + avgSearchCost / entry.getValue().size() + "\n");
        }
    }

    private void addToMaps(Stat statOne, Stat statTwo) {
        addToMapsHelper(testCaseInfoHeuristicOne, statOne);
        addToMapsHelper(testCaseInfoHeuristicTwo, statTwo);
    }

    private void addToMapsHelper(Map<Integer,ArrayList<Stat>> map, Stat stat) {
        ArrayList<Stat> list = map.containsKey(stat.getDepth()) ? map.get(stat.getDepth()) : new ArrayList<>();
        list.add(stat);
        map.put(stat.getDepth(), list);
    }

    private boolean isValidInput(int[][] board) {
        String boardToString = getBoardString(board);
        for(int i = 0; i < 9; i++) {
            if(boardToString.length() - boardToString.replace(String.valueOf(i), "").length() != 1)
                return false;
        }
        return true;
    }

    private String getBoardString(int[][] board) {
        String str = "";
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                str += String.valueOf(board[i][j]);
            }
        }
        return str;
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
