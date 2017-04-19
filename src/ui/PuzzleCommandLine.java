package ui;

import presenter.PuzzleCommandLinePresenter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PuzzleCommandLine {

    private final Scanner scanner = new Scanner(System.in);
    private final int RANDOM_CHOICE = 1;
    private final int SPECIFIC_CHOICE = 2;

    private PuzzleCommandLinePresenter presenter;

    public PuzzleCommandLine(PuzzleCommandLinePresenter presenter) {
        this.presenter = presenter;
    }

    public void showChoices() throws InputMismatchException {
        System.out.println("Choose a type input:");
        System.out.println("1. Randomly generated.");
        System.out.println("2. Specific Configuration.");
        // receive user decision
        int choice = getUserInput();
        if(choice == RANDOM_CHOICE)
            presenter.generateRandomPuzzleAndSolve();
        else {
            System.out.println("Enter the specific configuration: ");
            System.out.println("EXAMPLE -> 415208367");
            presenter.takeConfigurationAndSolve(scanner.next());
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
