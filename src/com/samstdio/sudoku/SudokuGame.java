package com.samstdio.sudoku;

import java.util.*;

public class SudokuGame {
    // restriction : board should be 2-D and diagonal

    private final int[][] board;
    private final int[][] solution;
    private final int BLANK;
    private final Set<Integer> numbers;

    public SudokuGame(int[][] board, int[][] solution, int nums, int blank) {
        this.board = board;
        this.solution = solution;
        this.BLANK = blank;

        int sum = 0;
        Set<Integer> numbers = new HashSet<>();

        for (int i = blank; i <= nums; i++) {
            sum += i;
            numbers.add(i);
        }

        numbers.remove(this.BLANK);
        this.numbers = numbers;
    }

    AssignResult assign(final int row, final int col, final int num) {
        if (num == this.BLANK)
            return AssignResult.NOT_ASSIGNABLE;

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length)
            return AssignResult.OUT_OF_BOARDER;

        if (board[row][col] != this.BLANK)
            return AssignResult.ALREADY_ASSIGNED;
        // merge with first if statement.
        if (!assignable(row, col, num)) {
            return AssignResult.NOT_ASSIGNABLE;
        }

        board[row][col] = num;
        recalculate();

        if (solved())
            return AssignResult.SOLVED;

        return AssignResult.OK;
    }

    private boolean assignable(final int row, final int col, final int num) {
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
        // has no blank
        for (int[] row : board) {
            for (int num : row) {
                if (this.BLANK == num)
                    return false;
            }
        }

        // more conditions.

        return true;
    }

    public Set<Integer> getPossibleNumbers() {
        return new HashSet(numbers);
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

    // codes related to game play.
    void play() {
        // CLI interactive game play
        boolean quit = false;
        Scanner input = new Scanner(System.in);
        String line;

        while (!solved() || quit) {
            line = input.nextLine();
            char c = line.charAt(0);
            Command cmd = COMMAND_MAP.get(Character.toLowerCase(c));

            if (null == cmd)
                continue;

            switch (cmd) {
                case HELP:
                    SudokuGame.help();
                    break;
                case ASSIGN:
                    String[] words = line.split(" ");
                    if (words.length != 4)  // a row col num == 4
                        continue;
                    int row = Integer.parseInt(words[1]);
                    int col = Integer.parseInt(words[2]);
                    int num = Integer.parseInt(words[3]);

                    AssignResult result = assign(row, col, num);
                    SudokuGame.printResult(result, row, col, num);

                    break;
                case PRINT:
                    this.printBoard();
                    break;
                case QUIT:
                    System.out.println("Goodbye");
                    quit = true;
                    break;
            }
        }
    }

    private static void printResult(AssignResult result, int row, int col, int num) {
        switch (result) {
            case OK:
                System.out.println(String.format("Assign (%d) to (%d) row, (%d) col.", num, row, col));
                break;
            case ALREADY_ASSIGNED:
                System.out.println(String.format("(%d) row, (%d) col is already assigned.", num, row, col));
                break;
            case NOT_ASSIGNABLE:
                System.out.println(String.format("Cannot assign (%d) to (%d) row, (%d) col.", num, row, col));
                break;
            case SOLVED:
                System.out.println("Congraturation. You've solved it!");
                break;
            case OUT_OF_BOARDER:
                System.out.println(String.format("(%d) row, (%d) col is out of board.", row, col));
                break;
        }
    }

    private static void help() {
        System.out.println("====== Sudoku Game ======");
        System.out.println("(a) Assign a number to row/column => a row col num");
        System.out.println("(p) Print current board");
        System.out.println("(q) Quit");
        System.out.println("(h) Help. Print this message.");

    }

    private static final Map<Character, Command> COMMAND_MAP = new HashMap<>();

    static {
        COMMAND_MAP.put('h', Command.HELP);
        COMMAND_MAP.put('a', Command.ASSIGN);
        COMMAND_MAP.put('p', Command.PRINT);
        COMMAND_MAP.put('q', Command.QUIT);
    }

    enum Command {
        HELP,
        ASSIGN,
        PRINT,
        QUIT
    }

    public static void main(String[] args) {
        final int[][] board_hard = {
                {4,2,0,5,0,0,0,7,0},
                {0,0,0,4,6,0,1,0,5},
                {0,6,0,9,1,0,0,3,0},
                {6,0,2,0,9,0,4,5,0},
                {7,0,5,2,0,0,9,6,0},
                {9,4,0,7,0,0,0,0,0},
                {0,5,0,0,7,0,3,0,0},
                {3,0,0,6,0,1,0,8,2},
                {0,0,9,0,0,0,0,0,0}
        };

        final int[][] solution_hard = {
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
        SudokuGame game = new SudokuGame(board_hard, solution_hard, 9,0);

        game.play();
    }

}