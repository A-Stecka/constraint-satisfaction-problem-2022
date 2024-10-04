package Constraint;

import java.util.ArrayList;
import java.util.HashSet;

public class UniqueRowsConstraint extends Constraint {

    public UniqueRowsConstraint(int[] variableIndexes) {
        this.variableIndexes = new ArrayList<>();
        for (int index : variableIndexes)
            this.variableIndexes.add(index);
    }

    // constraint has to be checked even if some values are unassigned
    @Override
    public boolean notBroken(int[] assignment) {
        int n = (int) Math.sqrt(variableIndexes.size());

        ArrayList<ArrayList<Integer>> allRows = new ArrayList<>();
        for (int i = 0; i < variableIndexes.size(); i++) {
            ArrayList<Integer> currentRow;
            if (i % n == 0) {
                currentRow = new ArrayList<>();
                allRows.add(currentRow);
            }
            else
                currentRow = allRows.get(i / n);
            currentRow.add(assignment[i]);
        }

        ArrayList<ArrayList<Integer>> fullRows = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (!allRows.get(i).contains(-1))
                fullRows.add(allRows.get(i));

        HashSet<ArrayList<Integer>> uniqueRows = new HashSet<>(fullRows);
        return uniqueRows.size() == fullRows.size();
    }

}
