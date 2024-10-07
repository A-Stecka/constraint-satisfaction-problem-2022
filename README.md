# Solving Binary Puzzles and Futoshiki as Constraint Satisfaction Problems
Binary and Futoshiki puzzle solver created as part of the Artificial Intelligence and Knowledge Engineering course
-  
Constraint Satisfaction Problem (CSP) refers to the problem of satisfying constraints. A solution to a CSP problem is an assignment of values to variables such that all constraints are satisfied. The constraints are defined for the specific problem.  

### Definition of CSP for Binary Puzzles:  
- Problem domain: n x n board,  
- Variables: n² fields of the board,  
- Domains of variables: {0, 1},  
- Constraints:  
  - Each row and column must have the same number of 0s and 1s — meaning that neither more 0s nor more 1s than half of n can appear in any row or column,  
  - Rows must be unique from one another,  
  - Columns must be unique from one another,  
  - No three consecutive values of 0 or 1 can appear either horizontally or vertically.  

### Definition of CSP for Futoshiki Puzzles:  
- Problem domain: n x n board,  
- Variables: n² fields of the board,  
- Domains of variables: {0, 1},  
- Constraints:  
  - A value cannot be repeated in a row,  
  - A value cannot be repeated in a column,  
  - Constraints arising from the >, < signs placed between the fields of the board.
