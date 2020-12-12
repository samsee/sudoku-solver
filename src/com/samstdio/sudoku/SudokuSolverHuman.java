package com.samstdio.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SudokuSolverHuman {
    private SudokuGame game;
    private List<int[]> blank_list;
    private Set<Integer> numbers;
    private int counter;

    public SudokuSolverHuman(SudokuGame game) {
        this.game = game;
        this.blank_list = new ArrayList(Arrays.asList(game.getBlankPosition()));
        this.numbers = game.getPossibleNumbers();
    }

    public void solve() {

    }
}
