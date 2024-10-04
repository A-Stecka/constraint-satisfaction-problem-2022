import Puzzle.*;

public class main {

    public static void binary_6x6(boolean variableHeuristic, boolean valueHeuristic) {
        int n = 6;
        DataReader reader = new DataReader(n, true, "binary_6x6");
        reader.readFromFile();
        Puzzle puzzle = new BinaryPuzzle(n);
        puzzle.initialisePuzzle(reader);

        CSPsolver solver = new CSPsolver(puzzle, variableHeuristic, valueHeuristic);
        solver.solveBacktracking();
        solver.printResults();
        solver.solveForwardChecking();
        solver.printResults();
        solver.solveFullLookAhead();
        solver.printResults();
    }

    public static void binary_8x8(boolean variableHeuristic, boolean valueHeuristic) {
        int n = 8;
        DataReader reader = new DataReader(n, true, "binary_8x8");
        reader.readFromFile();
        Puzzle puzzle = new BinaryPuzzle(n);
        puzzle.initialisePuzzle(reader);

        CSPsolver solver = new CSPsolver(puzzle, variableHeuristic, valueHeuristic);
        solver.solveBacktracking();
        solver.printResults();
        solver.solveForwardChecking();
        solver.printResults();
        solver.solveFullLookAhead();
        solver.printResults();
    }

    public static void binary_10x10(boolean variableHeuristic, boolean valueHeuristic) {
        int n = 10;
        DataReader reader = new DataReader(n, true, "binary_10x10");
        reader.readFromFile();
        Puzzle puzzle = new BinaryPuzzle(n);
        puzzle.initialisePuzzle(reader);

        CSPsolver solver = new CSPsolver(puzzle, variableHeuristic, valueHeuristic);
        solver.solveBacktracking();
        solver.printResults();
        solver.solveForwardChecking();
        solver.printResults();
        solver.solveFullLookAhead();
        solver.printResults();
    }

    public static void futoshiki_4x4(boolean variableHeuristic, boolean valueHeuristic) {
        int n = 4;
        DataReader reader = new DataReader(n, false, "futoshiki_4x4");
        reader.readFromFile();
        Puzzle puzzle = new FutoshikiPuzzle(n);
        puzzle.initialisePuzzle(reader);

        CSPsolver solver = new CSPsolver(puzzle, variableHeuristic, valueHeuristic);
        solver.solveBacktracking();
        solver.printResults();
        solver.solveForwardChecking();
        solver.printResults();
        solver.solveFullLookAhead();
        solver.printResults();
    }

    public static void futoshiki_5x5(boolean variableHeuristic, boolean valueHeuristic) {
        int n = 5;
        DataReader reader = new DataReader(n, false, "futoshiki_5x5");
        reader.readFromFile();
        Puzzle puzzle = new FutoshikiPuzzle(n);
        puzzle.initialisePuzzle(reader);

        CSPsolver solver = new CSPsolver(puzzle, variableHeuristic, valueHeuristic);
        solver.solveBacktracking();
        solver.printResults();
        solver.solveForwardChecking();
        solver.printResults();
        solver.solveFullLookAhead();
        solver.printResults();
    }

    public static void futoshiki_6x6(boolean variableHeuristic, boolean valueHeuristic) {
        int n = 6;
        DataReader reader = new DataReader(n, false, "futoshiki_6x6");
        reader.readFromFile();
        Puzzle puzzle = new FutoshikiPuzzle(n);
        puzzle.initialisePuzzle(reader);

        CSPsolver solver = new CSPsolver(puzzle, variableHeuristic, valueHeuristic);
        solver.solveBacktracking();
        solver.printResults();
        solver.solveForwardChecking();
        solver.printResults();
        solver.solveFullLookAhead();
        solver.printResults();
    }

    public static void solveAllGiven(boolean variableHeuristic, boolean valueHeuristic) {
        binary_6x6(variableHeuristic, valueHeuristic);
        binary_8x8(variableHeuristic, valueHeuristic);
        binary_10x10(variableHeuristic, valueHeuristic);
        futoshiki_4x4(variableHeuristic, valueHeuristic);
        futoshiki_5x5(variableHeuristic, valueHeuristic);
        futoshiki_6x6(variableHeuristic, valueHeuristic);
    }

    public static void solveAllBinary(boolean variableHeuristic, boolean valueHeuristic) {
        for (int n = 2; n < 9; n += 2) {
            DataReader reader = new DataReader(n, true, "research\\binary_" + n + "x" + n + ".txt");
            reader.readFromFile();
            Puzzle puzzle = new BinaryPuzzle(n);
            puzzle.initialisePuzzle(reader);

            CSPsolver solver = new CSPsolver(puzzle, variableHeuristic, valueHeuristic);
            solver.solveBacktracking();
            solver.printResults();
            solver.solveForwardChecking();
            solver.printResults();
            solver.solveFullLookAhead();
            solver.printResults();
        }
    }

    public static void solveAllFutoshiki(boolean variableHeuristic, boolean valueHeuristic) {
        for (int n = 2; n < 7; n++) {
            DataReader reader = new DataReader(n, false, "research\\futoshiki_" + n + "x" + n + ".txt");
            reader.readFromFile();
            Puzzle puzzle = new FutoshikiPuzzle(n);
            puzzle.initialisePuzzle(reader);

            CSPsolver solver = new CSPsolver(puzzle, variableHeuristic, valueHeuristic);
            solver.solveBacktracking();
            solver.printResults();
            solver.solveForwardChecking();
            solver.printResults();
            solver.solveFullLookAhead();
            solver.printResults();
        }
    }

    public static void solveAllEmpty(boolean variableHeuristic, boolean valueHeuristic) {
        solveAllBinary(variableHeuristic, valueHeuristic);
        solveAllFutoshiki(variableHeuristic, valueHeuristic);
    }

    public static void main(String[] args) {
        solveAllGiven(false, false);
//        solveAllEmpty(false, false);
    }
}
