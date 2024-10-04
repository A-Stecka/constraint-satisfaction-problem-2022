package Constraint;

import java.util.ArrayList;
import java.util.HashSet;

public class UniqueValuesConstraint extends Constraint {

    public UniqueValuesConstraint(int[] variableIndexes) {
        this.variableIndexes = new ArrayList<>();
        for (int index : variableIndexes)
            this.variableIndexes.add(index);
    }

    // constraint has to be checked even if some values are unassigned
    @Override
    public boolean notBroken(int[] assignment) {
        ArrayList<Integer> assigned = new ArrayList<>();
        HashSet<Integer> values = new HashSet<>();
        for (int index : variableIndexes)
            if (assignment[index] != -1) {
                assigned.add(index);
                values.add(assignment[index]);
            }
        return values.size() == assigned.size();
    }

}
