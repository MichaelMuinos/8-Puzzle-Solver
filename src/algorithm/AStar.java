package algorithm;

import javafx.util.Pair;
import model.PuzzleState;
import java.util.*;

public class AStar {

    public static final int HEURISTIC_ONE = 1;
    public static final int HEURISTIC_TWO = 2;

    private static PuzzleState goalState = new PuzzleState();
    private static Map<Integer,Pair<Integer,Integer>> coordinates = new HashMap<>();
    private final int INITIAL_CAPACITY = 10;

    private PuzzleStateComparator comparator;
    private Queue<PuzzleState> frontier;
    private Set<PuzzleState> visitedSet;

    static {
        int[][] goalBoard = new int[PuzzleState.SIZE][PuzzleState.SIZE];
        int count = 0;
        for(int i = 0; i < goalBoard.length; i++) {
            for(int j = 0; j < goalBoard[i].length; j++) {
                goalBoard[i][j] = count;
                coordinates.put(count, new Pair<>(i,j));
                ++count;
            }
        }
        goalState.setBoard(goalBoard);
    }

    public AStar() {
        comparator = new PuzzleStateComparator();
    }

    public void performAStar(PuzzleState initialState, int heuristic) {
        frontier = new PriorityQueue<>(INITIAL_CAPACITY, comparator);
        visitedSet = new HashSet<>();
        frontier.add(initialState);
        // while frontier is not empty
        while(!frontier.isEmpty()) {
            // find puzzle state with smallest f in the frontier
            PuzzleState leastCostPuzzleState = frontier.remove();
            System.out.println(leastCostPuzzleState);
            if(leastCostPuzzleState.equals(goalState)) return;
            // generate all possible successors
            List<PuzzleState> successors = getSuccessors(leastCostPuzzleState);
            for(int i = 0; i < successors.size(); i++) {
                PuzzleState successor = successors.get(i);
                // if the successor is equal to the goal state, stop the search
                if(successor.equals(goalState)) {
                    System.out.println(goalState);
                    return;
                }
                // parent g value + distance between successor and parent
                // increase moves by 1
                successor.setG(leastCostPuzzleState.getG() + 1);
                // heuristic value: distance from successor to goal
                successor.setH(determineHeuristicValue(successor, heuristic));
                // set f for successor
                successor.setF(successor.getG() + successor.getH());
                // if it has the same puzzle state in the visited set and has a lower
                // f than the successor, skip it
                if(!containsSuccessorAndHasLowerCost(successor))
                    frontier.add(successor);
            }
            // add to our visited set
            visitedSet.add(leastCostPuzzleState);
        }
    }

    private List<PuzzleState> getSuccessors(PuzzleState state) {
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
        // check swap left successor
        if(yPos - 1 > -1) {
            PuzzleState swapLeftState = new PuzzleState();
            int[][] swapLeftBoard = deepCopyBoard(state);
            swap(swapLeftBoard, xPos, yPos, xPos, yPos - 1);
            swapLeftState.setBoard(swapLeftBoard);
            successors.add(swapLeftState);
        }
        // check swap right successor
        if(yPos + 1 < PuzzleState.SIZE) {
            PuzzleState swapRightState = new PuzzleState();
            int[][] swapRightBoard = deepCopyBoard(state);
            swap(swapRightBoard, xPos, yPos, xPos, yPos + 1);
            swapRightState.setBoard(swapRightBoard);
            successors.add(swapRightState);
        }
        // check swap up successor
        if(xPos - 1 > -1) {
            PuzzleState swapUpState = new PuzzleState();
            int[][] swapUpBoard = deepCopyBoard(state);
            swap(swapUpBoard, xPos, yPos, xPos - 1, yPos);
            swapUpState.setBoard(swapUpBoard);
            successors.add(swapUpState);
        }
        // check swap down successor
        if(xPos + 1 < PuzzleState.SIZE) {
            PuzzleState swapDownState = new PuzzleState();
            int[][] swapDownBoard = deepCopyBoard(state);
            swap(swapDownBoard, xPos, yPos, xPos + 1, yPos);
            swapDownState.setBoard(swapDownBoard);
            successors.add(swapDownState);
        }
        return successors;
    }

    // used to swap the tile with the blank space
    private void swap(int[][] board, int xPos, int yPos, int xTilePos, int yTilePos) {
        int tileNumber = board[xTilePos][yTilePos];
        board[xTilePos][yTilePos] = 0;
        board[xPos][yPos] = tileNumber;
    }

    // performs a deep copy of the 2 dimensional board
    private int[][] deepCopyBoard(PuzzleState state) {
        return Arrays.stream(state.getBoard()).map(i -> i.clone()).toArray(int[][]::new);
    }

    private boolean containsSuccessorAndHasLowerCost(PuzzleState successor) {
        Iterator<PuzzleState> iterator = visitedSet.iterator();
        while(iterator.hasNext()) {
            PuzzleState state = iterator.next();
            if(state.equals(successor) && state.getF() < successor.getF())
                return true;
        }
        return false;
    }

    // determines the heuristic value of the successor by
    // identifying the number of misplaced tiles or distance from tile to goal
    private int determineHeuristicValue(PuzzleState successor, int heuristic) {
        int value = 0;
        int[][] board = successor.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(heuristic == HEURISTIC_ONE) {
                    if(board[i][j] != 0 && board[i][j] != goalState.getBoard()[i][j])
                        ++value;
                } else {
                    if(board[i][j] != 0)
                        value += Math.abs(i - coordinates.get(board[i][j]).getKey()) - Math.abs(j - coordinates.get(board[i][j]).getValue());
                }
            }
        }
        return value;
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
