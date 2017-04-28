package algorithm;

import data.Stat;
import javafx.util.Pair;

import java.text.DecimalFormat;
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

    public void performAStar(PuzzleState initialState) {
        System.out.println("\n*** NOTE: Heuristic one in some cases when attempting to solve a randomly" +
                "\ngenerated puzzle can take a really long time depending on the depth, so if it is taking awhile" +
                "\nto print out anything, it is because of heuristic one still performing the calculation. ***");
        System.out.println("\nCalculating...");
        performAStarHelper(initialState, HEURISTIC_ONE);
        performAStarHelper(initialState, HEURISTIC_TWO);
    }

    public Stat performAStar(PuzzleState initialState, int heuristic) {
        return performAStarHelper(initialState, heuristic);
    }

    private Stat performAStarHelper(PuzzleState initialState, int heuristic) {
        // start timer
        long startTime = System.currentTimeMillis();
        // end timer
        long endTime;
        // nodes generated
        int nodesGenerated = 0;
        // reset frontier, goal state, and visited set
        frontier = new PriorityQueue<>(INITIAL_CAPACITY, comparator);
        visitedSet = new HashSet<>();
        goalState.setParent(null);
        goalState.setF(0);
        // check to see if the initial state is the goal state
        if(initialState.equals(goalState)) {
            endTime = System.currentTimeMillis();
            return printAndReturnOptimalSolution(endTime - startTime, heuristic, 0);
        }
        // add the starting puzzle to the frontier
        frontier.add(initialState);
        // while frontier is not empty
        while(!frontier.isEmpty()) {
            // find puzzle state with smallest f in the frontier
            PuzzleState leastCostPuzzleState = frontier.remove();
            // generate all possible successors
            List<PuzzleState> successors = getSuccessors(leastCostPuzzleState);
            // add successor count to nodes generated
            nodesGenerated += successors.size();
            for(int i = 0; i < successors.size(); i++) {
                PuzzleState successor = successors.get(i);
                // if the successor is equal to the goal state, stop the search
                // and set the goal state parent
                if(successor.equals(goalState)) {
                    goalState.setParent(successor.getParent());
                    goalState.setF(successor.getF());
                    endTime = System.currentTimeMillis();
                    return printAndReturnOptimalSolution(endTime - startTime, heuristic, nodesGenerated);
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
                if(!containsSuccessorAndHasLowerCost(successor)) frontier.add(successor);
            }
            // add to our visited set
            visitedSet.add(leastCostPuzzleState);
        }
        return null;
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
        if(yPos - 1 > -1) getSuccessorsHelper(state, successors, xPos, yPos, xPos, yPos - 1);
        // check swap right successor
        if(yPos + 1 < PuzzleState.SIZE) getSuccessorsHelper(state, successors, xPos, yPos, xPos, yPos + 1);
        // check swap up successor
        if(xPos - 1 > -1) getSuccessorsHelper(state, successors, xPos, yPos,xPos - 1, yPos);
        // check swap down successor
        if(xPos + 1 < PuzzleState.SIZE) getSuccessorsHelper(state, successors, xPos, yPos, xPos + 1, yPos);
        return successors;
    }

    private void getSuccessorsHelper(PuzzleState state, List<PuzzleState> successors, int xPos, int yPos, int xTilePos, int yTilePos) {
        PuzzleState swapState = new PuzzleState();
        int[][] swapStateBoard = deepCopyBoard(state);
        swap(swapStateBoard, xPos, yPos, xTilePos, yTilePos);
        swapState.setBoard(swapStateBoard);
        swapState.setParent(state);
        successors.add(swapState);
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
                        value += Math.abs(i - coordinates.get(board[i][j]).getKey()) + Math.abs(j - coordinates.get(board[i][j]).getValue());
                }
            }
        }
        return value;
    }

    private Stat printAndReturnOptimalSolution(long time, int heuristic, int nodesGenerated) {
        // create optimal path list
        List<PuzzleState> optimalPath = new ArrayList<>();
        PuzzleState current = goalState;
        double completionTime = (double) time / 1000.0;
        int searchCost = 0;
        int depth = 0;
        // print heuristic message
        if(heuristic == HEURISTIC_ONE) System.out.println("\nINITIAL STATE W/ HEURISTIC ONE (MISPLACED TILES)");
        else System.out.println("\nINITIAL STATE W/ HEURISTIC TWO (MANHATTAN DISTANCE)");
        while(current != null) {
            optimalPath.add(current);
            current = current.getParent();
        }
        depth = (optimalPath.size() - 1);
        // print the solution
        for(int i = optimalPath.size() - 1; i >= 0; --i) {
            System.out.println(optimalPath.get(i));
            searchCost += optimalPath.get(i).getF();
        }
        // print time to run in seconds
        System.out.println("\nTime to run: " + new DecimalFormat("#.#######").format(completionTime) + " seconds");
        // print total steps
        System.out.println("Total steps: " + depth);
        // print number of nodes generated
        System.out.println("Nodes generated: " + nodesGenerated);
        // print total cost
        System.out.println("Search cost: " + searchCost);
        System.out.println("FINISHED\n\n---------------------------------------------------");
        return new Stat(depth, searchCost, nodesGenerated, completionTime);
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
