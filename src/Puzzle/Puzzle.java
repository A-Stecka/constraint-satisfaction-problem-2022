package Puzzle;

import Constraint.Constraint;

import java.util.ArrayList;

// abstract class for all puzzles - necessary to provide same algorithm for different puzzles
public abstract class Puzzle {
    public int n;
    public int[] puzzle;
    public int[] domain;
    public ArrayList<Constraint> constraints;

    // puzzle is initialised using DataReader as a data layer class
    public void initialisePuzzle(DataReader reader) {
        System.arraycopy(reader.puzzle, 0, puzzle, 0, puzzle.length);
        constraints.addAll(reader.constraints);
    }

}
