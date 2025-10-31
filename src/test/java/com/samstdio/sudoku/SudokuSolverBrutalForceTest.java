package com.samstdio.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for SudokuSolverBrutalForce class.
 *
 * This solver uses a brute force approach:
 * - Tries each possible number (1-9) for each blank cell
 * - Accepts the first number that passes validation
 * - Moves to the next blank cell
 * - This is simpler but may not solve all Sudoku puzzles optimally
 */
class SudokuSolverBrutalForceTest {

    private int[][] easyBoard;
    private int[][] easySolution;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Very simple board that brute force can solve
        easyBoard = new int[][] {
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

        easySolution = new int[][] {
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

        // Capture stdout to avoid cluttering test output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Should solve simple Sudoku puzzle")
    void testSolveSimplePuzzle() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);
        SudokuSolverBrutalForce solver = new SudokuSolverBrutalForce(game);

        // Count blanks before solving
        int blanksBefore = game.getBlankPosition().length;
        assertEquals(9, blanksBefore);

        // Solve
        solver.solve();

        // After solving, should have no blanks
        int blanksAfter = game.getBlankPosition().length;
        assertEquals(0, blanksAfter);

        // Board should be solved
        assertTrue(game.solved());

        // Restore stdout
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should fill correct values in blank cells")
    void testCorrectValues() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);
        SudokuSolverBrutalForce solver = new SudokuSolverBrutalForce(game);

        solver.solve();

        // Verify specific cells that were blank are now correct
        assertEquals(5, game.getNumber(0, 4)); // Was blank, should be 5
        assertEquals(6, game.getNumber(1, 8)); // Was blank, should be 6
        assertEquals(6, game.getNumber(2, 2)); // Was blank, should be 6

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should maintain non-blank cells unchanged")
    void testPreserveNonBlankCells() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);

        // Store original non-blank values
        int val1 = game.getNumber(0, 0);
        int val2 = game.getNumber(1, 0);
        int val3 = game.getNumber(5, 5);

        SudokuSolverBrutalForce solver = new SudokuSolverBrutalForce(game);
        solver.solve();

        // Non-blank cells should remain unchanged
        assertEquals(val1, game.getNumber(0, 0));
        assertEquals(val2, game.getNumber(1, 0));
        assertEquals(val3, game.getNumber(5, 5));

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should handle already solved puzzle")
    void testAlreadySolved() {
        // Create a board with no blanks
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

        SudokuSolverBrutalForce solver = new SudokuSolverBrutalForce(game);

        // Should handle gracefully (nothing to solve)
        assertDoesNotThrow(() -> solver.solve());

        // Should still be solved
        assertTrue(game.solved());

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should handle single blank cell")
    void testSingleBlank() {
        int[][] singleBlankBoard = new int[][] {
            {1,2,3,4,5,6,7,8,9},
            {7,8,9,1,2,3,4,5,6},
            {4,5,6,7,8,9,1,2,3},
            {2,3,4,5,6,7,8,9,1},
            {8,9,1,2,3,4,5,6,7},
            {5,6,7,8,9,1,2,3,4},
            {3,4,5,6,7,8,9,1,2},
            {9,1,2,3,4,5,6,7,8},
            {6,7,8,9,1,2,3,0,5}  // Single blank at [8][7]
        };

        SudokuGame game = new SudokuGame(singleBlankBoard, easySolution, 9, 0);
        assertEquals(1, game.getBlankPosition().length);

        SudokuSolverBrutalForce solver = new SudokuSolverBrutalForce(game);
        solver.solve();

        assertTrue(game.solved());
        assertEquals(4, game.getNumber(8, 7)); // Should be filled with 4

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should initialize with game state correctly")
    void testInitialization() {
        SudokuGame game = new SudokuGame(copyBoard(easyBoard), easySolution, 9, 0);
        SudokuSolverBrutalForce solver = new SudokuSolverBrutalForce(game);

        // Solver should be created without errors
        assertNotNull(solver);

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Brute force approach tries numbers in sequence")
    void testBruteForceStrategy() {
        // Create a puzzle where only one number works for a blank
        int[][] constrainedBoard = new int[][] {
            {1,2,3,4,5,6,7,8,0},  // Only 9 can go in last position
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

        SudokuGame game = new SudokuGame(constrainedBoard, solution, 9, 0);
        SudokuSolverBrutalForce solver = new SudokuSolverBrutalForce(game);

        solver.solve();

        // The brute force solver will try 1,2,3,4,5,6,7,8,9 until it finds 9 works
        assertEquals(9, game.getNumber(0, 8));
        assertTrue(game.solved());

        System.setOut(originalOut);
    }

    /**
     * Helper method to create a deep copy of the board
     * (since we're modifying it during solving)
     */
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}
