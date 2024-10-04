package Constraint;

import java.util.ArrayList;

public class CompareConstraint extends Constraint {
    private int firstIndex, secondIndex;

    public CompareConstraint(int[] variableIndexes, char sign) {
        this.variableIndexes = new ArrayList<>();
        for (int index : variableIndexes)
            this.variableIndexes.add(index);
        if (sign == '>') {
            firstIndex = variableIndexes[0];
            secondIndex = variableIndexes[1];
        } else {
            firstIndex = variableIndexes[1];
            secondIndex = variableIndexes[0];
        }
    }

    // no point checking constraint if values arent assigned
    @Override
    public boolean notBroken(int[] assignment) {
        if (assignment[firstIndex] != -1 && assignment[secondIndex] != -1)
            return assignment[firstIndex] > assignment[secondIndex];
        return true;
    }

    public void printInfo() {
        System.out.println("Variable with id [" + firstIndex + "] > variable with id [" + secondIndex + "]");
    }
}
