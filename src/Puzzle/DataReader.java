package Puzzle;

import Constraint.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// data layer class used to separate puzzle implementation from file representation
public class DataReader {
    private String filename;
    private int n;
    private boolean mode; //true - binary, false - futoshiki
    public int[] puzzle;
    public ArrayList<Constraint> constraints;

    public DataReader(int n, boolean mode, String filename) {
        this.filename = filename;
        this.n = n;
        this.mode = mode;
        this.puzzle = new int[n*n];
        Arrays.fill(puzzle, -1);
        this.constraints = new ArrayList<>();
    }

    public void readFromFile() {
        if (mode) {
            readBinaryFromFile();
            generateBinaryConstraints();
        }
        else {
            readFutoshikiFromFile();
            generateFutoshikiConstraints();
        }
    }

    // only values are read from file
    private void readBinaryFromFile() {
        StringBuilder dataStringBuilder = new StringBuilder("");
        try (Scanner reader = new Scanner(new File(filename))) {
            // reading each line from file and appending lines to one long string
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                line = line.replaceAll("(\\r|\\n)", "");
                dataStringBuilder.append(line);
            }
            String data = dataStringBuilder.toString();
            // iterating through every character in long string and saving values into puzzle
            for (int i = 0; i < data.length(); i++)
                if (data.charAt(i) != 'x')
                    puzzle[i] = Character.getNumericValue(data.charAt(i));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // all binary constraints have to be generated for corresponding variable indexes
    private void generateBinaryConstraints() {
        // getting column indexes from one dimensional array
        ArrayList<int[]> allColumnIndexes = new ArrayList<>();
        for (int i = 0; i < puzzle.length; i++) {
            int[] currentColumnIndexes;
            if (i / n == 0) {
                currentColumnIndexes = new int[n];
                allColumnIndexes.add(currentColumnIndexes);
            }
            else
                currentColumnIndexes = allColumnIndexes.get(i % n);
            currentColumnIndexes[i / n] = i;
        }
        // getting row indexes from one dimensional array
        ArrayList<int[]> allRowIndexes = new ArrayList<>();
        for (int i = 0; i < puzzle.length; i++) {
            int[] currentRowIndexes;
            if (i % n == 0) {
                currentRowIndexes = new int[n];
                allRowIndexes.add(currentRowIndexes);
            }
            else
                currentRowIndexes = allRowIndexes.get(i / n);
            currentRowIndexes[i % n] = i;
        }
        // generating count constraints for each column and row
        for (int[] columnIndexes : allColumnIndexes)
            constraints.add(new CountConstraint(columnIndexes));
        for (int[] rowIndexes : allRowIndexes)
            constraints.add(new CountConstraint(rowIndexes));
        // getting indexes for entire puzzle
        int[] puzzleIndexes = new int[n*n];
        for (int i = 0; i < puzzleIndexes.length; i++)
            puzzleIndexes[i] = i;
        // generating unique column and row constraints for all indexes
        constraints.add(new UniqueColumnsConstraint(puzzleIndexes));
        constraints.add(new UniqueRowsConstraint(puzzleIndexes));
        // generating neighbour constraints for variables in the same row
        for (int i = 0; i < puzzle.length - 2; i++)
            if (i / n == (i + 1) / n && i / n == (i + 2) / n)
                constraints.add(new NeighbourConstraint(new int[]{i, i + 1, i + 2}));
        // generating neighbour constraints for variables in the same column
        for (int i = 0; i < puzzle.length - 2*n; i++)
            if (i % n == (i + n) % n && i % n == (i + 2*n) % n)
                constraints.add(new NeighbourConstraint(new int[]{i, i + n, i + 2*n}));
    }

    // both data and constraints are read from file
    private void readFutoshikiFromFile() {
        ArrayList<String> data = new ArrayList<>();
        try (Scanner reader = new Scanner(new File(filename))) {
            while (reader.hasNextLine()) {
                // reading each line from file and appending lines to data array - each line is a new element of the data array
                String line = reader.nextLine();
                line = line.replaceAll("(\\r|\\n)", "");
                StringBuilder stringBuilder = new StringBuilder("");
                // adding spaces in between characters in sign only lines
                if (!line.contains("x")) {
                    for (int i = 0; i < line.length(); i++) {
                        stringBuilder.append(line.charAt(i));
                        if (i != line.length() - 1)
                            stringBuilder.append(" ");
                    }
                    line = stringBuilder.toString();
                }
                data.add(line);
            }
            // futoshikiIndex - index of variable in puzzle array
            // xModifier - number of sign only rows
            // yModifier - number of encountered spaces in sign only rows
            int futoshikiIndex = 0, xModifier = 0, yModifier = 0;
            for (int i = 0; i < data.size(); i++) {
                // even elements include values to copy
                if (i % 2 == 0)
                    for (int j = 0; j < data.get(i).length(); j++) {
                        // each x is an empty variable
                        if (data.get(i).charAt(j) == 'x')
                            futoshikiIndex++;
                        // each number is an instantiated variable
                        if (data.get(i).charAt(j) != 'x' && data.get(i).charAt(j) != '>' && data.get(i).charAt(j) != '<' && data.get(i).charAt(j) != '-') {
                            puzzle[futoshikiIndex] = Character.getNumericValue(data.get(i).charAt(j));
                            futoshikiIndex++;
                        }
                        // each sign is a constraint between two variables in the same row
                        if (data.get(i).charAt(j) == '>' || data.get(i).charAt(j) == '<')
                            constraints.add(new CompareConstraint(new int[]{futoshikiIndex - 1, futoshikiIndex}, data.get(i).charAt(j)));
                    }
                // odd elements are sign only rows
                else {
                    xModifier++;
                    for (int j = 0; j < data.get(i).length(); j++) {
                        // each sign is a constraint between two variables in the same column
                        if (data.get(i).charAt(j) == '>' || data.get(i).charAt(j) == '<') {
                            int x = i - xModifier, y = j - yModifier;
                            constraints.add(new CompareConstraint(new int[]{x*n + y, x*n + y + n}, data.get(i).charAt(j)));
                        }
                        if (data.get(i).charAt(j) == ' ')
                            yModifier++;
                    }
                    yModifier = 0;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // only unique values constraints have to be generated for corresponding variable indexes
    private void generateFutoshikiConstraints() {
        // getting column indexes from one dimensional array
        ArrayList<int[]> allColumnIndexes = new ArrayList<>();
        for (int i = 0; i < puzzle.length; i++) {
            int[] currentColumnIndexes;
            if (i / n == 0) {
                currentColumnIndexes = new int[n];
                allColumnIndexes.add(currentColumnIndexes);
            }
            else
                currentColumnIndexes = allColumnIndexes.get(i % n);
            currentColumnIndexes[i / n] = i;
        }
        // getting row indexes from one dimensional array
        ArrayList<int[]> allRowIndexes = new ArrayList<>();
        for (int i = 0; i < puzzle.length; i++) {
            int[] currentRowIndexes;
            if (i % n == 0) {
                currentRowIndexes = new int[n];
                allRowIndexes.add(currentRowIndexes);
            }
            else
                currentRowIndexes = allRowIndexes.get(i / n);
            currentRowIndexes[i % n] = i;
        }
        // generating unique values constraints for each column and row
        for (int[] columnIndexes : allColumnIndexes)
            constraints.add(new UniqueValuesConstraint(columnIndexes));
        for (int[] rowIndexes : allRowIndexes)
            constraints.add(new UniqueValuesConstraint(rowIndexes));
    }

}
