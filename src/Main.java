import algorithm.AStar;
import presenter.PuzzleCommandLinePresenter;
import ui.PuzzleCommandLine;

public class Main {

    public static void main(String[] args) {
        AStar aStar = new AStar();
        PuzzleCommandLinePresenter presenter = new PuzzleCommandLinePresenter(aStar);
        PuzzleCommandLine puzzleCommandLine = new PuzzleCommandLine(presenter);
        puzzleCommandLine.showChoices();
    }

}
