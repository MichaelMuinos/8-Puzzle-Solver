package ui;

import models.PuzzleState;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PuzzleCommandLine {

    private final Scanner scanner = new Scanner(System.in);
    private final int RANDOM_CHOICE = 1;
    private final int SPECIFIC_CHOICE = 2;

    public void showChoices() {
        System.out.println("Choose a type input:");
        System.out.println("1. Randomly generated.");
        System.out.println("2. Specific Configuration.");
        // receive user decision
        int choice = getUserInput();
        if(choice == RANDOM_CHOICE) {

        } else {

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
