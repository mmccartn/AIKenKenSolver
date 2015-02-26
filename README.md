## Overview
Solves arbitrary [KenKen](https://en.wikipedia.org/wiki/KenKen) puzzles, by representing the game as a Constraint Satisfaction Problem (**CSP**).

Most Constraining Variable (**MCV**) and Least Constraining Value (**LCV**) heuristic are used to backtrack 
from *a* solution and return all other possible solutions for a given puzzle.

## Representation of the KenKen Puzzles

### Board

The KenKen board is represented by a square n-by-n grid of cells. The grid may contain between 1 and n boxes
(cages) represented by a heavily outlined perimeter. Each cage will contain in superscript: the target digit value
for the cage followed by a mathematical operator. Each cell may contain any one of the digits: 1through n
inclusive. Please refer to Figure 1a for an example KenKen board representation.

![alt tag](https://raw.githubusercontent.com/mmccartn/AIKenKenSolver/master/figures/1.png)
