package com.samstdio.sudoku;

import java.util.*;

public class SudokuSolverHuman implements SudokuSolver {
    private SudokuGame game;
    private List<int[]> blank_list;
    private Set<Integer> numbers;
    private int counter;

    public SudokuSolverHuman(SudokuGame game) {
        this.game = game;
        this.blank_list = new ArrayList(Arrays.asList(game.getBlankPosition()));
        this.numbers = game.getPossibleNumbers();
    }

    @Override
    public void solve() {
        int index = 0;

        while (0 < blank_list.size()) {
            counter++;
            int[] blank = blank_list.get(index);

            boolean blank_solved = false;

            System.out.print(String.format("[%d] Trying (%d) row, (%d) col : ", counter, blank[0], blank[1]));

            blank_solved = solveSingleBlank(blank);

            if (0 == blank_list.size())
                break;

            if (!blank_solved) {
                System.out.println(String.format("\n\tCannot solve (%d) row (%d) col", blank[0], blank[1]));
                index++;
            }

            index %= blank_list.size();
        }

        System.out.println("Solved!");
        this.game.printBoard();
    }

    private boolean solveSingleBlank(final int[] row_col) {
        int row = row_col[0];
        int col = row_col[1];

        boolean solved = false;

        int[] possible_numbers = getCandidates(row, col);

        if (possible_numbers.length == 1) {
            switch (game.assign(row, col, possible_numbers[0])) {
                case OK:
                case SOLVED:
                    System.out.println(String.format("\n(%d) row (%d) col is (%d)", row, col, possible_numbers[0]));
                    blank_list.remove(row_col);
                    solved = true;
            }
        } else {
            // TODO more strategy
        }

        return solved;
    }

    /**
     * Find candidate numbers for row, col
     * @param row
     * @param col
     * @return
     */
    private int[] getCandidates(int row, int col) {
        Set<Integer> row_possibles = getRowPossible(row);
        if (row_possibles.size() == 1)
            return setToArray(row_possibles);

        Set<Integer> col_possibles = getColPossible(col);
        if (col_possibles.size() == 1)
            return setToArray(col_possibles);

        Set<Integer> sec_possibles = getSecPossible(row, col);
        if (sec_possibles.size() == 1)
            return setToArray(sec_possibles);

        Set numbers = game.getPossibleNumbers();

        numbers.retainAll(row_possibles);
        numbers.retainAll(col_possibles);
        numbers.retainAll(sec_possibles);

        if (numbers.size() == 1)
            return setToArray(numbers);
        else {
            // TODO more possibility?
            ;
        }

        return setToArray(numbers);

    }

    private Set<Integer> getSecPossible(int row, int col) {
        int[] sec_nums = game.getSection(row, col);
        return getPossibleNumbers(sec_nums);
    }

    private Set<Integer> getColPossible(int col) {
        int[] col_nums = game.getColumn(col);
        return getPossibleNumbers(col_nums);
    }

    private Set<Integer> getRowPossible(int row) {
        int[] row_nums = game.getRow(row);
        return getPossibleNumbers(row_nums);
    }

    private Set<Integer> getPossibleNumbers(final int[] numbers) {
        Set<Integer> possibles = game.getPossibleNumbers();

        for (int num : numbers)
            possibles.remove(num);

        return possibles;
    }

    private static int[] setToArray(final Set<Integer> int_set) {
        int[] ret = new int[int_set.size()];
        Iterator<Integer> iter = int_set.iterator();

        int i = 0;
        while (iter.hasNext())
            ret[i++] = iter.next();

        Arrays.sort(ret);

        return ret;
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

        // cannot solve
//        final int[][] board_hard = {
//                {3,0,6,0,0,0,0,8,0},
//                {0,0,0,0,4,0,2,0,0},
//                {1,0,0,0,0,2,0,0,7},
//                {0,9,0,6,0,0,0,0,0},
//                {0,0,7,0,1,0,4,0,0},
//                {0,0,0,0,0,3,0,5,0},
//                {5,0,0,8,0,0,0,0,1},
//                {0,0,4,0,2,0,0,0,0},
//                {0,8,0,0,0,0,9,0,3}
//        };
        // cannot solve
//        final int[][] board_hard = {
//                {0,0,0,0,0,1,0,0,2},
//                {0,0,0,0,0,6,8,5,0},
//                {0,0,0,2,8,0,0,9,0},
//                {0,0,2,5,0,0,0,6,4},
//                {0,0,8,0,4,0,2,0,0},
//                {7,3,0,0,0,9,1,0,0},
//                {0,5,0,0,6,2,0,0,0},
//                {0,1,7,9,0,0,0,0,0},
//                {8,0,0,4,0,0,0,0,0}
//        };

        // can solve
        final int[][] board_easy = {
                {4,2,0,5,0,0,0,7,0},
                {0,0,0,4,6,0,1,0,5},
                {0,6,0,9,1,0,0,3,0},
                {6,0,2,0,8,0,4,5,0},
                {7,0,5,2,0,0,9,6,0},
                {9,4,0,7,0,0,0,0,0},
                {0,5,0,0,7,0,3,0,0},
                {3,0,0,6,0,1,0,8,2},
                {0,0,9,0,0,0,0,0,0}
        };

        final int[][] solution_easy = {
                {4,2,1,5,3,8,6,7,9},
                {8,9,3,4,6,7,1,2,5},
                {5,6,7,9,1,2,8,3,4},
                {6,3,2,1,8,9,4,5,7},
                {7,1,5,2,4,3,9,6,8},
                {9,4,8,7,5,6,2,1,3},
                {2,5,6,8,7,4,3,9,1},
                {3,7,4,6,9,1,5,8,2},
                {1,8,9,3,2,5,7,4,6}
        };

        long start = System.currentTimeMillis();
        SudokuGame game = new SudokuGame(board_easy, solution_easy, 9,0);
        SudokuSolverHuman solver = new SudokuSolverHuman(game);

        solver.solve();

        System.out.println("Elapsed time in milliseconds : " + (System.currentTimeMillis() - start));
    }


}
