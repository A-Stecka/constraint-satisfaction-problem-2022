package Constraint;

import java.util.ArrayList;

public class CountConstraint extends Constraint {

    public CountConstraint(int[] variableIndexes) {
        this.variableIndexes = new ArrayList<>();
        for (int index : variableIndexes)
            this.variableIndexes.add(index);
    }

    // constraint has to be checked even if some values are unassigned
    // constraint is broken when there are more zeroes / ones than half of n
    @Override
    public boolean notBroken(int[] assignment) {
        int noOfZeroes = 0, noOfOnes = 0;
        ArrayList<Integer> values = new ArrayList<>();
        for (Integer variableIndex : variableIndexes)
            if (assignment[variableIndex] != -1)
                values.add(assignment[variableIndex]);

        for (Integer value : values)
            if (value == 1)
                noOfOnes++;
            else
                noOfZeroes++;

        return noOfZeroes <= Math.sqrt(assignment.length) / 2 && noOfOnes <= Math.sqrt(assignment.length) / 2;
    }

}
