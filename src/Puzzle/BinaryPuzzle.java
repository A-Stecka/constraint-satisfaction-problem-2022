package Puzzle;

import java.util.ArrayList;

// binary puzzle class - the domain is {0, 1}
public class BinaryPuzzle extends Puzzle {

    public BinaryPuzzle(int n) {
        this.n = n;
        this.puzzle = new int[n*n];
        this.domain = new int[]{0, 1};
        this.constraints = new ArrayList<>();
    }

}
