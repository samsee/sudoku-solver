package com.samstdio.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SudokuSolverBrutalForce {
    private SudokuGame game;
    private List<int[]> blank_list;
    private Set<Integer> numbers;

    public SudokuSolverBrutalForce(SudokuGame game) {
        this.game = game;
        this.blank_list = new ArrayList<>(Arrays.asList(game.getBlankPosition()));
        this.numbers = game.getPossibleNumbers();
    }

    public void solve() {
        int index = 0;

        while (0 < blank_list.size()) {
            int[] blank = blank_list.get(index);

            boolean blank_solved = false;

            System.out.println();

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
            switch (game.assign(row, col, num)) {
                case OK:
                case SOLVED:
                    System.out.println();
                    blank_list.remove(row_col);
                    solved = true;
                    break;
            }

            if (solved)
                break;
        }


        return solved;
    }
}
