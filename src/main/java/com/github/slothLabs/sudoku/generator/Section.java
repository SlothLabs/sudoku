package com.github.slothLabs.sudoku.generator;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Indicates a section (i.e. row, column, or block) of
 * a Sudoku board.
 */
public class Section {

    public static final int DEFAULT_SIZE = 9;

    private final int size;

    private final Integer[] values;

    public Section() {
        this(DEFAULT_SIZE);
    }

    public Section(final int size) {
        this.size = size;

        this.values = new Integer[size];
    }

    public int getSize() {
        return size;
    }

    public boolean isComplete() {
        return (getNumberOfValues() == size);
    }

    public boolean isValid() {
        return (isComplete() && Arrays.stream(values)
                                        .allMatch(new HashSet<>()::add));
    }

    public int getNumberOfValues() {
        return (int)(Arrays.stream(values)
                            .filter(v -> v != null)
                            .count()
                    );
    }

    public void setValue(final int index, final int value) {
        values[index] = value;
    }

    public void clearValue(final int index) {
        values[index] = null;
    }

    public Integer getValueAt(final int index) {
        return values[index];
    }

    public int getNumberOfPossibles() {
        return size;
    }
}
