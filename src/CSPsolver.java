import Puzzle.*;
import Constraint.*;

import java.util.ArrayList;
import java.util.Collections;

// CSP solver class
public class CSPsolver {
    private int n;
    private Puzzle puzzle;
    private boolean variableHeuristic, valueHeuristic;
    private ArrayList<ArrayList<Integer>> domains;
    private ArrayList<ArrayList<Constraint>> constraints;
    private ArrayList<int[]> foundSolutions;
    private int visitedNodes;
    private long startTime, elapsedTime;
    private boolean backtracking, forwardChecking, fullLookAhead;

    public CSPsolver(Puzzle puzzle, boolean variableHeuristic, boolean valueHeuristic) {
        this.puzzle = puzzle;
        this.variableHeuristic = variableHeuristic;
        this.valueHeuristic = valueHeuristic;
        n = puzzle.n;
        domains = new ArrayList<>();
        constraints = new ArrayList<>();
        foundSolutions = new ArrayList<>();
        visitedNodes = 0;
        startTime = elapsedTime = 0;
        backtracking = forwardChecking = fullLookAhead = false;

        for (int i = 0; i < n*n; i++) {
            // generating list for domains for all variables - access to variable domain through domain.get(variableIndex)
            ArrayList<Integer> variableDomain = new ArrayList<>();
            for (int j = 0; j < puzzle.domain.length; j++)
                variableDomain.add(puzzle.domain[j]);
            domains.add(variableDomain);
            // generating list of constraints for all variables - access to constraints for variable through constraints.get(variableIndex)
            ArrayList<Constraint> variableConstraints = new ArrayList<>();
            for (Constraint constraint : puzzle.constraints)
                if (constraint.variableIndexes.contains(i))
                    variableConstraints.add(constraint);
            constraints.add(variableConstraints);
        }
    }

    public void solveBacktracking() {
        backtracking = true;
        forwardChecking = fullLookAhead = false;
        foundSolutions = new ArrayList<>();
        visitedNodes = 0;
        startTime = System.nanoTime();
        solveBacktracking(puzzle.puzzle);
        elapsedTime = System.nanoTime() - startTime;
    }

    public void solveForwardChecking() {
        forwardChecking = true;
        backtracking = fullLookAhead = false;
        foundSolutions = new ArrayList<>();
        visitedNodes = 0;
        startTime = System.nanoTime();
        solveForwardChecking(puzzle.puzzle);
        elapsedTime = System.nanoTime() - startTime;
    }

    public void solveFullLookAhead() {
        fullLookAhead = true;
        backtracking = forwardChecking = false;
        foundSolutions = new ArrayList<>();
        visitedNodes = 0;
        startTime = System.nanoTime();
        solveFullLookAhead(puzzle.puzzle);
        elapsedTime = System.nanoTime() - startTime;
    }

    // checking that all constraints for given variable are not broken
    private boolean checkVariableConstraints(int variableIndex, int[] assignment) {
        for (Constraint constraint : constraints.get(variableIndex))
            if (!constraint.notBroken(assignment))
                return false;
        return true;
    }

    // checking that all given constraints are not broken
    private boolean checkGivenConstraints(ArrayList<Constraint> givenConstraints, int[] assignment) {
        for (Constraint constraint : givenConstraints)
            if (!constraint.notBroken(assignment))
                return false;
        return true;
    }

    // solve puzzle using backtracking method for solving CSPs
    private void solveBacktracking(int[] previousAssignment) {
        // generating list of unassigned variables
        ArrayList<Integer> unassigned = new ArrayList<>();
        for (int i = 0; i < previousAssignment.length; i++)
            if (previousAssignment[i] == -1)
                unassigned.add(i);
        // if there are no unassigned variables the puzzle is solved
        if (unassigned.size() == 0) {
            foundSolutions.add(previousAssignment);
            return;
        }
        // random order heuristic - indexes are shuffled
        if (variableHeuristic)
            Collections.shuffle(unassigned);
        int firstUnassignedIndex = unassigned.get(0);
        // random order heuristic - values in domain are shuffled
        if (valueHeuristic)
            Collections.shuffle(domains.get(firstUnassignedIndex));
        // each value in variable domain is checked
        for (int value : domains.get(firstUnassignedIndex)) {
            visitedNodes++;
            // copying of previous assignment and instantiation of variable
            int[] temporaryAssignment = new int[n*n];
            System.arraycopy(previousAssignment, 0, temporaryAssignment, 0, previousAssignment.length);
            temporaryAssignment[firstUnassignedIndex] = value;
            // if no constraints are broken the next variable can be assigned
            if (checkVariableConstraints(firstUnassignedIndex, temporaryAssignment))
                solveBacktracking(temporaryAssignment);
        }
    }

    // solve puzzle using forward-checking method for solving CSPs
    private void solveForwardChecking(int[] previousAssignment) {
        // generating list of unassigned variables
        ArrayList<Integer> unassigned = new ArrayList<>();
        for (int i = 0; i < previousAssignment.length; i++)
            if (previousAssignment[i] == -1)
                unassigned.add(i);
        // if there are no unassigned variables the puzzle is solved
        if (unassigned.size() == 0) {
            foundSolutions.add(previousAssignment);
            return;
        }
        // random order heuristic - indexes are shuffled
        if (variableHeuristic)
            Collections.shuffle(unassigned);
        int firstUnassignedIndex = unassigned.get(0);
        // random order heuristic - values in domain are shuffled
        if (valueHeuristic)
            Collections.shuffle(domains.get(firstUnassignedIndex));
        // each value in variable domain is checked
        for (int value : domains.get(firstUnassignedIndex)) {
            visitedNodes++;
            // copying of previous assignment and instantiation of variable
            int[] temporaryAssignment = new int[n * n];
            System.arraycopy(previousAssignment, 0, temporaryAssignment, 0, previousAssignment.length);
            temporaryAssignment[firstUnassignedIndex] = value;
            // if no constraints are broken constraints for current variable and non instantiated variables are checked
            if (checkVariableConstraints(firstUnassignedIndex, temporaryAssignment)) {
                ArrayList<Constraint> variableConstraints = constraints.get(firstUnassignedIndex);
                ArrayList<Integer> constrainedVariableIndexes = new ArrayList<>();
                // finding constraints for current variable and non instantiated variables
                for (Constraint constraint : variableConstraints)
                    for (int variableIndex : constraint.variableIndexes) {
                        if (variableIndex != firstUnassignedIndex && temporaryAssignment[variableIndex] == -1 && !constrainedVariableIndexes.contains(variableIndex))
                            constrainedVariableIndexes.add(variableIndex);
                    }
                // previous domains are saved to allow for a backtrack
                // a deep copy is necessary because values from the domains will be removed
                ArrayList<ArrayList<Integer>> previousDomains = new ArrayList<>();
                for (ArrayList<Integer> domain : domains) {
                    previousDomains.add(new ArrayList<>());
                    for (int element : domain)
                        previousDomains.get(previousDomains.size() - 1).add(element);
                }
                // a backtrack will occur if foundEmptyDomain is true
                boolean foundEmptyDomain = false;
                // all variables appearing in constraints with current variable have to have their domains checked
                for (int variableIndex : constrainedVariableIndexes) {
                    // if one non instantiated variable domain is empty no other non instantiated variable domains have to be checked
                    if (!foundEmptyDomain) {
                        // finding constraints for current variable and current non instantiated variable
                        ArrayList<Constraint> constraintsForCurrentAndFutureVariable = new ArrayList<>();
                        for (Constraint constraint : variableConstraints)
                            if (constraint.variableIndexes.contains(variableIndex) && constraint.variableIndexes.contains(firstUnassignedIndex))
                                constraintsForCurrentAndFutureVariable.add(constraint);
                        // each value in the variable domain has to be checked against the found constraints
                        for (int nonInstantiatedValue : previousDomains.get(variableIndex)) {
                            int[] localAssignment = new int[n * n];
                            System.arraycopy(temporaryAssignment, 0, localAssignment, 0, temporaryAssignment.length);
                            localAssignment[variableIndex] = nonInstantiatedValue;
                            // if a constraint is broken the currently checked value is removed from the non instantiated variable domain
                            if (!checkGivenConstraints(constraintsForCurrentAndFutureVariable, localAssignment))
                                domains.get(variableIndex).remove((Integer) nonInstantiatedValue);
                        }
                        // if a non instantiated variable domain is empty a backtrack will occur
                        if (domains.get(variableIndex).size() == 0)
                            foundEmptyDomain = true;
                    }
                }
                // if no non instantiated variable domains are empty the next variable can be assigned
                if (!foundEmptyDomain)
                    solveForwardChecking(temporaryAssignment);
                // previous domains are restored after a backtrack
                // a deep copy is not necessary because when domains are saved a deep copy will be created
                for (int i = 0; i < previousDomains.size(); i++)
                    domains.set(i, previousDomains.get(i));
            }
        }
    }

    // solve puzzle using full look ahead method for solving CSPs
    private void solveFullLookAhead(int[] previousAssignment) {
        // generating list of unassigned variables
        ArrayList<Integer> unassigned = new ArrayList<>();
        for (int i = 0; i < previousAssignment.length; i++)
            if (previousAssignment[i] == -1)
                unassigned.add(i);
        // if there are no unassigned variables the puzzle is solved
        if (unassigned.size() == 0) {
            foundSolutions.add(previousAssignment);
            return;
        }
        // random order heuristic - indexes are shuffled
        if (variableHeuristic)
            Collections.shuffle(unassigned);
        int firstUnassignedIndex = unassigned.get(0);
        // random order heuristic - values in domain are shuffled
        if (valueHeuristic)
            Collections.shuffle(domains.get(firstUnassignedIndex));
        // each value in variable domain is checked
        for (int value : domains.get(firstUnassignedIndex)) {
            visitedNodes++;
            // copying of previous assignment and instantiation of variable
            int[] temporaryAssignment = new int[n * n];
            System.arraycopy(previousAssignment, 0, temporaryAssignment, 0, previousAssignment.length);
            temporaryAssignment[firstUnassignedIndex] = value;
            // if no constraints are broken constraints for current variable and non instantiated variables are checked
            if (checkVariableConstraints(firstUnassignedIndex, temporaryAssignment)) {
                ArrayList<Constraint> variableConstraints = constraints.get(firstUnassignedIndex);
                ArrayList<Integer> constrainedVariableIndexes = new ArrayList<>();
                // finding constraints for current variable and non instantiated variables
                for (Constraint constraint : variableConstraints)
                    for (int variableIndex : constraint.variableIndexes) {
                        if (variableIndex != firstUnassignedIndex && temporaryAssignment[variableIndex] == -1 && !constrainedVariableIndexes.contains(variableIndex))
                            constrainedVariableIndexes.add(variableIndex);
                    }
                // previous domains are saved to allow for a backtrack
                // a deep copy is necessary because values from the domains will be removed
                ArrayList<ArrayList<Integer>> previousDomains = new ArrayList<>();
                for (ArrayList<Integer> domain : domains) {
                    previousDomains.add(new ArrayList<>());
                    for (int element : domain)
                        previousDomains.get(previousDomains.size() - 1).add(element);
                }
                // a backtrack will occur if foundEmptyDomain is true
                boolean foundEmptyDomain = false;
                // all variables appearing in constraints with current variable have to have their domains checked
                for (int variableIndex : constrainedVariableIndexes) {
                    // if one non instantiated variable domain is empty no other non instantiated variable domains have to be checked
                    if (!foundEmptyDomain) {
                        // finding constraints for current variable and current non instantiated variable
                        ArrayList<Constraint> constraintsForCurrentAndFutureVariable = new ArrayList<>();
                        for (Constraint constraint : variableConstraints)
                            if (constraint.variableIndexes.contains(variableIndex) && constraint.variableIndexes.contains(firstUnassignedIndex))
                                constraintsForCurrentAndFutureVariable.add(constraint);
                        // each value in the variable domain has to be checked against the found constraints
                        for (int nonInstantiatedValue : previousDomains.get(variableIndex)) {
                            int[] localAssignment = new int[n * n];
                            System.arraycopy(temporaryAssignment, 0, localAssignment, 0, temporaryAssignment.length);
                            localAssignment[variableIndex] = nonInstantiatedValue;
                            // if a constraint is broken the currently checked value is removed from the non instantiated variable domain
                            if (!checkGivenConstraints(constraintsForCurrentAndFutureVariable, localAssignment))
                                domains.get(variableIndex).remove((Integer) nonInstantiatedValue);
                        }
                        // if a non instantiated variable domain is empty a backtrack will occur
                        if (domains.get(variableIndex).size() == 0)
                            foundEmptyDomain = true;
                        // if a non instantiated variable domain has size 1 constraints are propagated for this variable
                        if (domains.get(variableIndex).size() == 1) {
                            int[] localAssignment = new int[n * n];
                            System.arraycopy(temporaryAssignment, 0, localAssignment, 0, temporaryAssignment.length);
                            localAssignment[variableIndex] = domains.get(variableIndex).get(0);
                            if (!propagateConstraints(localAssignment, variableIndex))
                                foundEmptyDomain = true;
                        }
                    }
                }
                // if no non instantiated variable domains are empty the next variable can be assigned
                if (!foundEmptyDomain)
                    solveFullLookAhead(temporaryAssignment);
                // previous domains are restored after a backtrack
                // a deep copy is not necessary because when domains are saved a deep copy will be created
                for (int i = 0; i < previousDomains.size(); i++)
                    domains.set(i, previousDomains.get(i));
            }
        }
    }

    // method returns true if no variable domain is empty when constraints are propagated for one of the non instantiated variables
    private boolean propagateConstraints(int[] previousAssignment, int futureVariableIndex) {
        // generating list of unassigned variables
        ArrayList<Integer> unassigned = new ArrayList<>();
        for (int i = 0; i < previousAssignment.length; i++)
            if (previousAssignment[i] == -1)
                unassigned.add(i);
        // if there are no unassigned variables no non instantiated variable domain can be empty
        if (unassigned.size() == 0)
            return true;
        ArrayList<Constraint> variableConstraints = constraints.get(futureVariableIndex);
        ArrayList<Integer> constrainedVariableIndexes = new ArrayList<>();
        // finding constraints for current variable and non instantiated variables
        for (Constraint constraint : variableConstraints)
            for (int variableIndex : constraint.variableIndexes) {
                if (variableIndex != futureVariableIndex && previousAssignment[variableIndex] == -1 && !constrainedVariableIndexes.contains(variableIndex))
                    constrainedVariableIndexes.add(variableIndex);
            }
        // previous domains are saved to allow for a backtrack
        // a deep copy is necessary because values from the domains will be removed
        ArrayList<ArrayList<Integer>> previousDomains = new ArrayList<>();
        for (ArrayList<Integer> domain : domains) {
            previousDomains.add(new ArrayList<>());
            for (int element : domain)
                previousDomains.get(previousDomains.size() - 1).add(element);
        }
        // all variables appearing in constraints with current variable have to have their domains checked
        for (int variableIndex : constrainedVariableIndexes) {
            // finding constraints for current variable and current non instantiated variable
            ArrayList<Constraint> constraintsForCurrentAndFutureVariable = new ArrayList<>();
            for (Constraint constraint : variableConstraints)
                if (constraint.variableIndexes.contains(variableIndex) && constraint.variableIndexes.contains(futureVariableIndex))
                    constraintsForCurrentAndFutureVariable.add(constraint);
            // each value in the variable domain has to be checked against the found constraints
            for (int nonInstantiatedValue : previousDomains.get(variableIndex)) {
                int[] temporaryAssignment = new int[n * n];
                System.arraycopy(previousAssignment, 0, temporaryAssignment, 0, previousAssignment.length);
                temporaryAssignment[variableIndex] = nonInstantiatedValue;
                // if a constraint is broken the currently checked value is removed from the non instantiated variable domain
                if (!checkGivenConstraints(constraintsForCurrentAndFutureVariable, temporaryAssignment))
                    domains.get(variableIndex).remove((Integer) nonInstantiatedValue);
            }
            // if a non instantiated variable domain is empty assigning the value for which constraints are propagated to the variable will result in a backtrack
            if (domains.get(variableIndex).size() == 0) {
                // previous domains are restored because a backtrack will occur
                // a deep copy is not necessary because when domains are saved a deep copy will be created
                for (int i = 0; i < previousDomains.size(); i++)
                    domains.set(i, previousDomains.get(i));
                return false;
            }
            // if a non instantiated variable domain has size 1 constraints are propagated for this variable
            if (domains.get(variableIndex).size() == 1) {
                int[] temporaryAssignment = new int[n * n];
                System.arraycopy(previousAssignment, 0, temporaryAssignment, 0, previousAssignment.length);
                temporaryAssignment[variableIndex] = domains.get(variableIndex).get(0);
                return propagateConstraints(temporaryAssignment, variableIndex);
            }
        }
        return true;
    }

    public void printResults() {
        System.out.println("-----------------------------------------------------------");

        if (backtracking)
            System.out.println("SOLVING USING BACKTRACKING");
        if (forwardChecking)
            System.out.println("SOLVING USING FORWARD-CHECKING");
        if (fullLookAhead)
            System.out.println("SOLVING USING FULL LOOK AHEAD");

        if (variableHeuristic)
            System.out.println("USING RANDOM VARIABLE HEURISTIC");
        if (valueHeuristic)
            System.out.println("USING RANDOM VALUE HEURISTIC");

        if (puzzle instanceof BinaryPuzzle)
            System.out.println("SOLVING BINARY PUZZLE " + n + "x" + n);
        else
            System.out.println("SOLVING FUTOSHIKI PUZZLE " + n + "x" + n);

        System.out.println("Found solutions: " + foundSolutions.size());
        if (foundSolutions.size() != 0) {
            System.out.println("Sample solution:");
            for (int i = 0; i < foundSolutions.get(0).length; i++) {
                if (i != 0 && i % n == 0)
                    System.out.println();
                System.out.printf("%2d", foundSolutions.get(0)[i]);
                System.out.print(" ");
            }
        }
        System.out.println();

        System.out.println("Visited nodes: " + visitedNodes);
        System.out.println("Elapsed time: " + (double) elapsedTime / 1000000.0 + " [miliseconds]");
        System.out.println();
    }

}
