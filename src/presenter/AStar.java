package presenter;

import models.PuzzleState;

import java.util.*;

public class AStar {

    private final int INITIAL_CAPACITY = 10;

    private PuzzleStateComparator comparator;
    private PuzzleState puzzleState;
    private Queue<PuzzleState> frontier;
    private Set<PuzzleState> visitedSet;

    public AStar(PuzzleState puzzleState) {
        this.puzzleState = puzzleState;
        comparator = new PuzzleStateComparator();
        frontier = new PriorityQueue<>(INITIAL_CAPACITY, comparator);
        visitedSet = new HashSet<>();
    }

    public void performAStar() {
        frontier.add(puzzleState);
        // while frontier is not empty
        while(!frontier.isEmpty()) {
            // find puzzle state with smallest f in the frontier

        }
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
