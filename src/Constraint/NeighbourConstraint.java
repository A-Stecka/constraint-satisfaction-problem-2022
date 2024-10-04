package Constraint;

import java.util.ArrayList;

public class NeighbourConstraint extends Constraint {

    public NeighbourConstraint(int[] variableIndexes) {
        this.variableIndexes = new ArrayList<>();
        for (int index : variableIndexes)
            this.variableIndexes.add(index);
    }

    // no point checking constraint if values arent assigned
    // three neighbouring values cannot be the same
    @Override
    public boolean notBroken(int[] assignment) {
        ArrayList<Integer> values = new ArrayList<>();
        for (Integer variableIndex : variableIndexes) {
            if (assignment[variableIndex] == -1)
                return true;
            values.add(assignment[variableIndex]);
        }
        return !values.get(0).equals(values.get(1)) || !values.get(0).equals(values.get(2));
    }

}
