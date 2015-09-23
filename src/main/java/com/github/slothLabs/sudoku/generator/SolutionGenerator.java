package com.github.slothLabs.sudoku.generator;

public class SolutionGenerator {

    public int getPuzzleSize() {
        return 9;
    }

    public SudokuSolution generate() {
        return new SudokuSolution();
    }
}
