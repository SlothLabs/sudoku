package com.github.slothLabs.sudoku.generator;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolutionGeneratorTest {

    @Test
    public void noArgsConstructorShouldSetPuzzleSizeToNine() {
        final SolutionGenerator solutionGenerator = new SolutionGenerator();

        assertEquals(9, solutionGenerator.getPuzzleSize());
    }

    @Test
    public void generateShouldCreateValidSudokuSolution() {
        final SolutionGenerator solutionGenerator = new SolutionGenerator();

        final SudokuSolution solution = solutionGenerator.generate();
        assertTrue(solution.isComplete());
        assertTrue(solution.isValid());
    }
}
