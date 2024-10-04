package Constraint;

import java.util.ArrayList;
import java.util.HashSet;

public class UniqueColumnsConstraint extends Constraint {

    public UniqueColumnsConstraint(int[] variableIndexes) {
        this.variableIndexes = new ArrayList<>();
        for (int index : variableIndexes)
            this.variableIndexes.add(index);
    }

    // constraint has to be checked even if some values are unassigned
    @Override
    public boolean notBroken(int[] assignment) {
        int n = (int) Math.sqrt(variableIndexes.size());

        ArrayList<ArrayList<Integer>> allColumns = new ArrayList<>();
        for (int i = 0; i < variableIndexes.size(); i++) {
            ArrayList<Integer> currentColumn;
            if (i / n == 0) {
                currentColumn = new ArrayList<>();
                allColumns.add(currentColumn);
            }
            else
                currentColumn = allColumns.get(i % n);
            currentColumn.add(assignment[i]);
        }

        ArrayList<ArrayList<Integer>> fullColumns = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (!allColumns.get(i).contains(-1))
               fullColumns.add(allColumns.get(i));

        HashSet<ArrayList<Integer>> uniqueColumns = new HashSet<>(fullColumns);
        return uniqueColumns.size() == fullColumns.size();
    }

}
