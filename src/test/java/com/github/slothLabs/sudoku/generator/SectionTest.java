package com.github.slothLabs.sudoku.generator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SectionTest {

    private Section section;

    @Before
    public void setup() {
        section = new Section();
    }

    @Test
    public void constructorShouldSetAppropriateSize() {
        final int expSize = 3;
        final Section argConstructorSection = new Section(expSize);

        assertEquals(expSize, argConstructorSection.getSize());
    }

    @Test
    public void noArgConstructorShouldSetSizeToNine() {
        final int expSize = 9;

        assertEquals(expSize, section.getSize());
    }

    @Test
    public void initialNumberOfPossiblesShouldEqualSize() {
        final int count = section.getSize();

        final int availables = section.getNumberOfPossibles();
        assertEquals(count, availables);
    }

    @Test
    public void aNewSectionShouldNotBeComplete() {
        assertFalse(section.isComplete());
    }

    @Test
    public void aNewSectionShouldNotBeValid() {
        assertFalse(section.isValid());
    }

    @Test
    public void aNewSectionShouldHaveZeroValues() {
        assertEquals(0, section.getNumberOfValues());
    }

    @Test
    public void settingValuesInASectionShouldIncreaseTheNumberOfValues() {
        section.setValue(0, 3);
        section.setValue(1, 4);

        assertEquals(2, section.getNumberOfValues());
    }

    @Test
    public void isCompleteShouldReturnTrueWhenNumberOfValuesEqualsSize() {
        for (int i = 0; i < section.getSize(); ++i) {
            section.setValue(i, i + 1);
        }

        assertEquals(section.getSize(), section.getNumberOfValues());
        assertTrue(section.isComplete());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void settingAValueAtIndexOfSizeShouldThrowException() {
        section.setValue(section.getSize(), 9);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void settingAValueAtNegativeIndexShouldThrowException() {
        section.setValue(-5, 10);
    }

    @Test
    public void clearingASetValueShouldReduceNumberOfValues() {
        section.setValue(0, 3);
        section.setValue(1, 4);

        assertEquals(2, section.getNumberOfValues());

        section.clearValue(1);

        assertEquals(1, section.getNumberOfValues());
    }

    @Test
    public void isValidShouldReturnFalseIfNotComplete() {
        section.setValue(0, 3);
        section.setValue(1, 4);

        assertFalse(section.isComplete());
        assertFalse(section.isValid());
    }

    @Test
    public void getValueAtShouldReturnNullForUnsetValue() {
        section.setValue(0, 2);
        section.setValue(1, 3);

        final Integer idx2 = section.getValueAt(2);
        assertNull(idx2);
    }

    @Test
    public void getValueAtShouldReturnSetValue() {
        final int idx0Val = 3;
        final int idx1Val = 4;
        section.setValue(0, idx0Val);
        section.setValue(1, idx1Val);

        final Integer idx1 = section.getValueAt(1);
        assertNotNull(idx1);
        assertEquals(idx1.intValue(), idx1Val);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getValueAtShouldThrowIfPassedNegativeIndex() {
        section.getValueAt(-5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getValueAtShouldThrowIfPassedIndexOfSize() {
        section.getValueAt(section.getSize());
    }

    @Test
    public void isValidShouldReturnFalseIfAnyDuplicatesArePresent() {
        for (int i = 0; i < section.getSize(); ++i) {
            section.setValue(i, i + 1);
        }

        section.setValue(5, section.getValueAt(6));

        assertFalse(section.isValid());
    }

    @Test
    public void isValidShouldReturnTrueIfCompleteWithoutDuplicates() {
        for (int i = 0; i < section.getSize(); ++i) {
            section.setValue(i, i + 1);
        }

        assertTrue(section.isValid());
    }
}