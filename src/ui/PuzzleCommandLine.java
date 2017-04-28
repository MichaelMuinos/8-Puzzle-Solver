package ui;

import presenter.PuzzleCommandLinePresenter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PuzzleCommandLine {

    private final Scanner scanner = new Scanner(System.in);
    private final int RANDOM_CHOICE = 1;
    private final int SPECIFIC_CHOICE = 2;
    private final int FILE_INPUT_CHOICE = 3;
    private final int EXIT_CHOICE = 4;

    private PuzzleCommandLinePresenter presenter;

    public PuzzleCommandLine(PuzzleCommandLinePresenter presenter) {
        this.presenter = presenter;
    }

    public void showChoices() throws InputMismatchException {
        boolean stop = false;
        while (!stop) {
            System.out.println("\nChoose a type input:");
            System.out.println("1. Randomly generated.");
            System.out.println("2. Specific configuration.");
            System.out.println("3. File input (used to create table data)");
            System.out.println("4. Exit");
            // receive user decision
            int choice = getUserChoice();
            switch (choice) {
                case RANDOM_CHOICE:
                    presenter.generateRandomPuzzleAndSolve();
                    break;
                case SPECIFIC_CHOICE:
                    System.out.println("Enter the specific configuration one by one starting in the top row \n(valid entries are 0-8 where 0 is the empty space): ");
                    boolean valid = false;
                    while (!valid) {
                        int[][] board = presenter.generateBoard(getUserInput());
                        valid = presenter.takeConfigurationAndSolve(board);
                        if (!valid) System.out.println("Uh oh! Either the input is invalid or it is not solvable. Try again!");
                    }
                    break;
                case FILE_INPUT_CHOICE:
                    System.out.println("The file must contain lines in a flat order. You must enter the absolute path of the file!\nEXAMPLE -> 412356780");
                    System.out.println("Enter the file name: ");
                    presenter.processFile(scanner.next());
                    break;
                case EXIT_CHOICE:
                    stop = true;
                    System.out.println("Peace out!");
                    break;
                default:
                    break;
            }
        }
    }

    private int[] getUserInput() throws InputMismatchException {
        int[] board = new int[9];
        for(int i = 0; i < board.length; i++) {
            board[i] = scanner.nextInt();
            printBoard(board);
        }
        return board;
    }

    private void printBoard(int[] board) {
        int count = 0;
        for(int i = 0; i < board.length; i++) {
            if(count == 3) {
                System.out.println();
                count = 0;
            }
            System.out.print(board[i] + " ");
            ++count;
        }
        System.out.println();
    }

    private int getUserChoice() throws InputMismatchException {
        int choice = 0;
        boolean validChoice = false;
        while(!validChoice) {
            choice = scanner.nextInt();
            if(choice == RANDOM_CHOICE || choice == SPECIFIC_CHOICE || choice == FILE_INPUT_CHOICE || choice == EXIT_CHOICE)
                validChoice = true;
            else
                System.out.println("Incorrect input. Try again!");
        }
        return choice;
    }

}
