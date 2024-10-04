package Puzzle;

import java.util.ArrayList;

// futoshiki puzzle class - the domain is {1, 2, ..., n}
public class FutoshikiPuzzle extends Puzzle {

    public FutoshikiPuzzle(int n) {
        this.n = n;
        this.puzzle = new int[n*n];
        this.domain = new int[n];
        for (int i = 0; i < n; i++)
            domain[i] = i + 1;
        this.constraints = new ArrayList<>();
    }

}
