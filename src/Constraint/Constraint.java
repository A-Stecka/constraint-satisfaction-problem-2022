package Constraint;

import java.util.ArrayList;

// abstract class for all constraints - necessary to provide same algorithm for different puzzles
public abstract class Constraint {
    public ArrayList<Integer> variableIndexes;

    // sometimes you cannot determine whether a constraint is satisfied or not because of non instantiated variables
    // method returns false when a constraint is broken
    public abstract boolean notBroken(int[] assignment);
}
