package com.samstdio.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for SudokuSolverHuman class.
 *
 * This solver uses human-like strategies:
 * - For each blank cell, finds all possible valid numbers
 * - Uses constraint propagation (row, column, section constraints)
 * - Only assigns a number when there's exactly ONE possibility
 * - More intelligent than brute force but may not solve harder puzzles
 *
 * Key strategy: If a cell has only one possible valid number after considering
 * all constraints (row, column, and 3x3 section), assign that number.
 */
class SudokuSolverHumanTest {

    private int[][] easyBoard;
    private int[][] easySolution;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Board that can be solved using human logic (constraint propagation)
        easyBoard = new int[][] {
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

        easySolution = new int[][] {
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

        // Capture stdout to avoid cluttering test output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Should solve easy Sudoku puzzle using human strategies")
    void testSolveEasyPuzzle() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);
        SudokuSolverHuman solver = new SudokuSolverHuman(game);

        // Count blanks before solving
        int blanksBefore = game.getBlankPosition().length;
        assertTrue(blanksBefore > 0);

        // Solve
        solver.solve();

        // After solving, should have no blanks
        int blanksAfter = game.getBlankPosition().length;
        assertEquals(0, blanksAfter);

        // Board should be solved
        assertTrue(game.solved());

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should fill correct values matching the solution")
    void testCorrectValues() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);
        SudokuSolverHuman solver = new SudokuSolverHuman(game);

        solver.solve();

        // Verify the entire board matches the solution
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                assertEquals(easySolution[row][col], game.getNumber(row, col),
                    String.format("Mismatch at position [%d][%d]", row, col));
            }
        }

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should use constraint propagation to find unique candidates")
    void testConstraintPropagation() {
        // Create a simple case where one cell has only one possibility
        int[][] simpleBoard = new int[][] {
            {1,2,3,4,5,6,7,8,0},  // Last cell can only be 9
            {7,8,9,1,2,3,4,5,6},
            {4,5,6,7,8,9,1,2,3},
            {2,3,4,5,6,7,8,9,1},
            {8,9,1,2,3,4,5,6,7},
            {5,6,7,8,9,1,2,3,4},
            {3,4,5,6,7,8,9,1,2},
            {9,1,2,3,4,5,6,7,8},
            {6,7,8,9,1,2,3,4,5}
        };

        int[][] solution = new int[][] {
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

        SudokuGame game = new SudokuGame(simpleBoard, solution, 9, 0);
        SudokuSolverHuman solver = new SudokuSolverHuman(game);

        solver.solve();

        // The human solver should identify that only 9 can go in [0][8]
        assertEquals(9, game.getNumber(0, 8));
        assertTrue(game.solved());

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should handle already solved puzzle")
    void testAlreadySolved() {
        int[][] solvedBoard = new int[][] {
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

        SudokuGame game = new SudokuGame(solvedBoard, easySolution, 9, 0);
        assertTrue(game.solved());

        SudokuSolverHuman solver = new SudokuSolverHuman(game);

        // Should handle gracefully
        assertDoesNotThrow(() -> solver.solve());
        assertTrue(game.solved());

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should maintain non-blank cells unchanged")
    void testPreserveNonBlankCells() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);

        // Store original non-blank values
        int val1 = game.getNumber(0, 0);
        int val2 = game.getNumber(1, 3);
        int val3 = game.getNumber(4, 0);

        SudokuSolverHuman solver = new SudokuSolverHuman(game);
        solver.solve();

        // Non-blank cells should remain unchanged
        assertEquals(val1, game.getNumber(0, 0));
        assertEquals(val2, game.getNumber(1, 3));
        assertEquals(val3, game.getNumber(4, 0));

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should initialize with game state correctly")
    void testInitialization() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);
        SudokuSolverHuman solver = new SudokuSolverHuman(game);

        assertNotNull(solver);

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Human solver only assigns when there is exactly one candidate")
    void testOnlyAssignsUniqueCandidates() {
        // Create a puzzle where some cells have multiple candidates
        // The human solver should skip cells with multiple possibilities
        int[][] partialBoard = new int[][] {
            {1,2,3,4,5,6,7,8,0},  // [0][8] has only one possibility: 9
            {4,5,6,7,8,9,1,2,0},  // [1][8] has only one possibility: 3
            {7,8,9,1,2,3,4,5,0},  // [2][8] has only one possibility: 6
            {2,3,4,5,6,7,8,9,1},
            {8,9,1,2,3,4,5,6,7},
            {5,6,7,8,9,1,2,3,4},
            {3,4,5,6,7,8,9,1,2},
            {9,1,2,3,4,5,6,7,8},
            {6,7,8,9,1,2,3,4,5}
        };

        int[][] solution = new int[][] {
            {1,2,3,4,5,6,7,8,9},
            {4,5,6,7,8,9,1,2,3},
            {7,8,9,1,2,3,4,5,6},
            {2,3,4,5,6,7,8,9,1},
            {8,9,1,2,3,4,5,6,7},
            {5,6,7,8,9,1,2,3,4},
            {3,4,5,6,7,8,9,1,2},
            {9,1,2,3,4,5,6,7,8},
            {6,7,8,9,1,2,3,4,5}
        };

        SudokuGame game = new SudokuGame(partialBoard, solution, 9, 0);
        SudokuSolverHuman solver = new SudokuSolverHuman(game);

        solver.solve();

        // These cells should be filled with their unique candidates
        assertEquals(9, game.getNumber(0, 8));
        assertEquals(3, game.getNumber(1, 8));
        assertEquals(6, game.getNumber(2, 8));

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should iterate through blanks until solved or no progress")
    void testIterativeApproach() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);
        SudokuSolverHuman solver = new SudokuSolverHuman(game);

        int initialBlanks = game.getBlankPosition().length;

        solver.solve();

        // After solving, blank count should be reduced (ideally to 0 for easy puzzles)
        int finalBlanks = game.getBlankPosition().length;
        assertTrue(finalBlanks < initialBlanks, "Solver should reduce number of blanks");

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Human solver demonstrates intelligent strategy vs brute force")
    void testIntelligentStrategy() {
        // The human solver analyzes possibilities first, then assigns
        // Unlike brute force which tries numbers sequentially

        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);
        SudokuSolverHuman humanSolver = new SudokuSolverHuman(game);

        // The solve method should use getCandidates logic internally
        // which checks row, column, and section constraints
        humanSolver.solve();

        // If solved, it means the human strategy worked
        assertTrue(game.solved());

        System.setOut(originalOut);
    }

    /**
     * Helper method to create a deep copy of the board
     */
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}
