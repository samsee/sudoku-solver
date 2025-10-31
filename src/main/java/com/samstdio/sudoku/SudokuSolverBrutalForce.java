package com.samstdio.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SudokuSolverBrutalForce implements SudokuSolver {
    private SudokuGame game;
    private List<int[]> blank_list;
    private Set<Integer> numbers;
    private int counter;

    public SudokuSolverBrutalForce(SudokuGame game) {
        this.game = game;
        this.blank_list = new ArrayList<>(Arrays.asList(game.getBlankPosition()));
        this.numbers = game.getPossibleNumbers();
    }

    @Override
    public void solve() {
        int index = 0;

        while (0 < blank_list.size()) {
            counter++;
            int[] blank = blank_list.get(index);

            boolean blank_solved = false;

            System.out.print(String.format("Trying (%d) row, (%d) col : ", blank[0], blank[1]));

            blank_solved = solveSingleBlank(blank);

            if (0 == blank_list.size())
                break;

            if (!blank_solved) {
                index++;
            }
            index %= blank_list.size();

            System.out.println("Solved!");
            this.game.printSolution();
        }
    }

    private boolean solveSingleBlank(final int[] row_col) {
        int row = row_col[0];
        int col = row_col[1];

        boolean solved = false;

        // Brutal force strategy
        // Assign any number that is accepted.
        for (int num : numbers) {
            System.out.print(num + " ");
            switch (game.assign(row, col, num)) {
                case OK:
                case SOLVED:
                    System.out.println(String.format("\n%d,%d is %d", row, col, num));
                    blank_list.remove(row_col);
                    solved = true;
                    break;
            }

            if (solved)
                break;
        }

        return solved;
    }

    public static void main(String[] args) {
        final int[][] board = {
                {1,2,3,4,0,6,7,8,9},
                {7,8,9,1,2,3,4,5,0},
                {4,5,0,7,8,9,1,2,3},
                {2,3,4,5,6,0,8,9,1},
                {8,9,1,2,3,4,0,6,7},
                {5,0,7,8,9,1,2,3,4},
                {0,4,5,6,7,8,9,1,2},
                {9,1,2,0,4,5,6,7,8},
                {6,7,8,9,1,2,3,0,5}
        };
        final int[][] solution = {
                {1,2,3,4,5,6,7,8,9},
                {7,8,9,1,2,3,4,5,6},
                {4,5,6,7,8,9,1,2,3},
                {2,3,4,5,6,7,8,9,1},
                {8,9,1,2,3,4,5,6,7},
                {5,6,7,8,9,1,2,3,4},
                {3,4,5,6,7,8,9,1,2},
                {9,1,2,3,4,5,6,7,8},
                {6,7,8,9,1,2,3,4,5}
        };

        SudokuGame game = new SudokuGame(board, solution, 9,0);
        SudokuSolver solver = new SudokuSolverBrutalForce(game);
        solver.solve();
    }
}
