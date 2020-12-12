package com.samstdio.sudoku;

import java.util.*;

public class SudokuGame {
    // restriction : board should be 2-D and diagonal

    private final int[][] board;
    private final int[][] solution;
    private final int BLANK;
    private final Set<Integer> numbers; // can be enum?
    private final int sum; // remove if unused..

    public SudokuGame(int[][] board, int[][] solution, int nums, int blank) {
        this.board = board;
        this.solution = solution;
        this.BLANK = blank;

        int sum = 0;
        Set<Integer> numbers = new HashSet<Integer>();

        for (int i = blank; i <= nums; i++) {
            sum += i;
            numbers.add(i);
        }

        numbers.remove(this.BLANK);
        this.numbers = Collections.unmodifiableSet(numbers);

        this.sum = sum;
    }

    AssignResult assign(final int num, final int row, final int col) {
        if (num == this.BLANK)
            return AssignResult.NOT_ASSIGNABLE;

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length)
            return AssignResult.OUT_OF_BOARDER;

        if (board[row][col] != this.BLANK)
            return AssignResult.ALREADY_ASSIGNED;
        // merge with first if statement.
        if (!assignable(num, row, col)) {
            return AssignResult.NOT_ASSIGNABLE;
        }

        board[row][col] = num;
        recalculate();

        if (solved())
            return AssignResult.SOLVED;

        return AssignResult.OK;
    }

    private boolean assignable(final int num, final int row, final int col) {
        // TODO
        int[] row_nums = getRow(row);
        int[] col_nums = getColumn(col);
        int[] sec_nums = getSection(row, col);

        int row_index = indexOf(row_nums, num);
        int col_index = indexOf(col_nums, num);
        int sec_index = indexOf(sec_nums, num);

        if (row_index >= 0 || col_index >= 0 || sec_index >= 0)
            return false;

        return true;
    }

    public int getNumber(final int row, final int col) {
        return board[row][col];
    }

    private int indexOf(final int[] arr, final int num) {
        for (int i = 0; i < arr.length; i++)
            if (num == arr[i])
                return i;

        return -1;
    }

    int[] getSection(int row, int col) {
        // FIXME
        int section_row_start = (row / 3) * 3;
        int section_col_start = (col / 3) * 3;

        int[] section = new int[board[0].length];
        int k = 0;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                section[k++] = board[i + section_row_start][j + section_col_start];

        return section;
    }

    int[] getColumn(int col) {
        int[] cols = new int[board[0].length];

        for (int i = 0; i < board[0].length; i++)
            cols[i] = board[i][col];

        return cols;
    }

    int[] getRow(int row) {
        return Arrays.copyOf(board[row], board[row].length);
    }

    void recalculate() {
        // TODO
    }

    boolean solved() {
        for (int[] row : board) {
            for (int num : row) {
                if (this.BLANK == num)
                    return false;
            }
        }

        return true;
    }

    public Set<Integer> getPossibleNumbers() {
        return null;
    }

    enum AssignResult {
        OK,
        ALREADY_ASSIGNED,
        NOT_ASSIGNABLE,
        OUT_OF_BOARDER,
        SOLVED
    }

    public int[][] getBlankPosition() {
        List<int[]> blank_list = new ArrayList<int[]>();

        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[0].length; col++) {
                if (this.BLANK == this.board[row][col])
                    blank_list.add(new int[]{row, col});
            }
        }

        int[][] ret_arr = new int[blank_list.size()][2];

        return blank_list.toArray(ret_arr);
    }

    public void printBoard() {
        printArray(this.board);
    }

    public void printSolution() {
        printArray(this.solution);
    }

    private void printArray(final int[][] arr) {
        for (int[] row : arr) {
            for (int i : row) {
                System.out.print(String.format("%d\t", i));
            }
            System.out.println();
        }
    }

    void play() {
        // CLI interactive
        // TODO
    }

    private static Map<Character, Command> COMMAND_MAP = new HashMap<Character, Command>();

    enum Command {
        HELP,
        ASSIGN,
        PRINT,
        QUIT
    }


}