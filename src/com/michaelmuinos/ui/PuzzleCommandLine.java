package com.michaelmuinos.ui;

import model.PuzzleState;
import algorithm.AStar;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PuzzleCommandLine {

    private final Scanner scanner = new Scanner(System.in);
    private final int RANDOM_CHOICE = 1;
    private final int SPECIFIC_CHOICE = 2;
    private final AStar aStar = new AStar();

    public void showChoices() {
        System.out.println("Choose a type input:");
        System.out.println("1. Randomly generated.");
        System.out.println("2. Specific Configuration.");
        // receive user decision
        int choice = getUserInput();
        if(choice == RANDOM_CHOICE) {
            PuzzleState puzzleState = new PuzzleState();
            puzzleState.generateRandomBoard();
            aStar.performAStar(puzzleState, AStar.HEURISTIC_ONE);
        } else {
            PuzzleState puzzleState = new PuzzleState();
            int[][] b = new int[3][3];
            String str = scanner.next();
            int count = 0;
            for(int i = 0; i < b.length; i++) {
                for(int j = 0; j < b[i].length; j++) {
                    b[i][j] = Character.getNumericValue(str.charAt(count));
                    ++count;
                }
            }
            puzzleState.setBoard(b);
            aStar.performAStar(puzzleState, AStar.HEURISTIC_ONE);
            System.out.println("\n\n\n\n\n\nFINISHED");
            aStar.performAStar(puzzleState, AStar.HEURISTIC_TWO);
        }
    }

    private int getUserInput() throws InputMismatchException {
        int choice = 0;
        boolean validChoice = false;
        while(!validChoice) {
            choice = scanner.nextInt();
            if(choice == RANDOM_CHOICE || choice == SPECIFIC_CHOICE)
                validChoice = true;
            else
                System.out.println("Incorrect input. Try again!");
        }
        return choice;
    }

}
