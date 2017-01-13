package com.github.slothLabs.sudoku.generator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Indicates a section (i.e. row, column, or block) of
 * a Sudoku board.
 */
public class Section {

    public static final int DEFAULT_SIZE = 9;

    private final int size;

    private final Integer[] values;
    private final Set<Integer> possibles;

    public Section() {
        this(DEFAULT_SIZE);
    }

    public Section(final int size) {
        this.size = size;

        this.values = new Integer[size];
        this.possibles = new HashSet<>(size);
        for (int i = 1; i <= size; ++i) {
            this.possibles.add(i);
        }
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
        if (possibles.contains(value)) {
            values[index] = value;
            possibles.remove(value);
        }
    }

    public void clearValueAt(final int index) {
        final Integer val = values[index];
        possibles.add(val);
        values[index] = null;
    }

    public Integer getValueAt(final int index) {
        return values[index];
    }

    public int getNumberOfPossibles() {
        return size;
    }

    public Set<Integer> getPossibles() {
        return Collections.unmodifiableSet(possibles);
    }
}
