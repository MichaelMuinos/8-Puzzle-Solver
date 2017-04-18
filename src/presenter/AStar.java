package presenter;

import models.PuzzleState;
import java.util.*;

public class AStar {

    private static PuzzleState goalState = new PuzzleState();
    private final int INITIAL_CAPACITY = 10;

    private PuzzleStateComparator comparator;
    private PuzzleState initialState;
    private Queue<PuzzleState> frontier;
    private Set<PuzzleState> visitedSet;

    static {
        int[][] goalBoard = new int[PuzzleState.SIZE][PuzzleState.SIZE];
        int count = 0;
        for(int i = 0; i < goalBoard.length; i++) {
            for(int j = 0; j < goalBoard[i].length; j++) {
                goalBoard[i][j] = count;
                ++count;
            }
        }
        goalState.setBoard(goalBoard);
    }

    public AStar(PuzzleState initialState) {
        this.initialState = initialState;
        comparator = new PuzzleStateComparator();
        frontier = new PriorityQueue<>(INITIAL_CAPACITY, comparator);
        visitedSet = new HashSet<>();
    }

    public void performAStar() {
        frontier.add(initialState);
        // while frontier is not empty
        while(!frontier.isEmpty()) {
            // find puzzle state with smallest f in the frontier
            PuzzleState leastCostPuzzleState = frontier.remove();
            // generate all possible successors
            List<PuzzleState> successors = getSuccessors(leastCostPuzzleState);
            for(int i = 0; i < successors.size(); i++) {
                PuzzleState successor = successors.get(i);
                // if the successor is equal to the goal state, stop the search
                if(successor.equals(goalState)) return;
                // parent g value + distance between successor and parent
                // increase moves by 1
                successor.setG(leastCostPuzzleState.getG() + 1);
                // heuristic value: distance from successor to goal
                successor.setH(determineHeuristicOneValue(successor));
                // set f for successor
                successor.setF(successor.getG() + successor.getH());
                // if it has the same puzzle state in the frontier and has a lower
                // f than the successor, skip it
                if(!containsSuccessorAndHasLowerCost(successor, true) || !containsSuccessorAndHasLowerCost(successor, false))
                    frontier.add(successor);
            }
            // add to our visited set
            visitedSet.add(leastCostPuzzleState);
        }
    }

    public List<PuzzleState> getSuccessors(PuzzleState state) {
        List<PuzzleState> successors = new ArrayList<>();
        int[][] board = state.getBoard();
        int xPos = 0;
        int yPos = 0;
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 0) {
                    xPos = i;
                    yPos = j;
                    break;
                }
            }
        }
        System.out.println(state);
        // check swap left successor
        if(yPos - 1 > -1) {
            PuzzleState swapLeftState = new PuzzleState();
            int[][] swapLeftBoard = deepCopyBoard(state);
            swap(swapLeftBoard, xPos, yPos, xPos, yPos - 1);
            swapLeftState.setBoard(swapLeftBoard);
        }
        // check swap right successor
        if(yPos + 1 < PuzzleState.SIZE) {
            PuzzleState swapRightState = new PuzzleState();
        }
        return successors;
    }

    private void swap(int[][] board, int xPos, int yPos, int xTilePos, int yTilePos) {
        int tileNumber = board[xTilePos][yTilePos];
        board[xTilePos][yTilePos] = 0;
        board[xPos][yPos] = tileNumber;
    }

    // performs a deep copy of the 2 dimensional board
    private int[][] deepCopyBoard(PuzzleState state) {
        return Arrays.stream(state.getBoard()).map(i -> i.clone()).toArray(int[][]::new);
    }

    private boolean containsSuccessorAndHasLowerCost(PuzzleState successor, boolean isFrontier) {
        Iterator<PuzzleState> iterator = isFrontier ? frontier.iterator() : visitedSet.iterator();
        while(iterator.hasNext()) {
            PuzzleState state = iterator.next();
            if(state.equals(successor) && state.getF() < successor.getF())
                return true;
        }
        return false;
    }

    // determines the heuristic value of the successor by
    // identifying the number of misplaced tiles
    private int determineHeuristicOneValue(PuzzleState successor) {
        int[][] board = successor.getBoard();
        int misplacedTiles = 0;
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != 0 && board[i][j] != goalState.getBoard()[i][j])
                    ++misplacedTiles;
            }
        }
        return misplacedTiles;
    }

    // determines the heuristic value of the successor by
    // calculating the sum of the Manhattan distance of each tile
    private int determineHeuristicTwoValue(PuzzleState successor) {
        return 0;
    }

    // custom comparator class to sort PuzzleState objects
    // by their least cost f
    public class PuzzleStateComparator implements Comparator<PuzzleState> {
        @Override
        public int compare(PuzzleState o1, PuzzleState o2) {
            return o1.getF() - o2.getF();
        }
    }

}
