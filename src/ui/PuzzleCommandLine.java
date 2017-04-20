package ui;

import presenter.PuzzleCommandLinePresenter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PuzzleCommandLine {

    private final Scanner scanner = new Scanner(System.in);
    private final int RANDOM_CHOICE = 1;
    private final int SPECIFIC_CHOICE = 2;
    private final int EXIT_CHOICE = 3;

    private PuzzleCommandLinePresenter presenter;

    public PuzzleCommandLine(PuzzleCommandLinePresenter presenter) {
        this.presenter = presenter;
    }

    public void showChoices() throws InputMismatchException {
        boolean stop = false;
        while(!stop) {
            System.out.println("\nChoose a type input:");
            System.out.println("1. Randomly generated.");
            System.out.println("2. Specific Configuration.");
            System.out.println("3. Exit");
            // receive user decision
            int choice = getUserInput();
            if (choice == RANDOM_CHOICE)
                presenter.generateRandomPuzzleAndSolve();
            else if(choice == SPECIFIC_CHOICE){
                System.out.println("Enter the specific configuration: ");
                System.out.println("EXAMPLE -> 415208367");
                System.out.println("The above input is equivalent to:\n4 1 5\n2 0 8\n3 6 7");
                System.out.println("Puzzle: ");
                while (!presenter.takeConfigurationAndSolve(scanner.next()))
                    System.out.println("Uh oh! Either the input is invalid or it is not solvable. Try again!");
            } else {
                stop = true;
                System.out.println("Peace out!");
            }
        }
    }

    private int getUserInput() throws InputMismatchException {
        int choice = 0;
        boolean validChoice = false;
        while(!validChoice) {
            choice = scanner.nextInt();
            if(choice == RANDOM_CHOICE || choice == SPECIFIC_CHOICE || choice == EXIT_CHOICE)
                validChoice = true;
            else
                System.out.println("Incorrect input. Try again!");
        }
        return choice;
    }

}
