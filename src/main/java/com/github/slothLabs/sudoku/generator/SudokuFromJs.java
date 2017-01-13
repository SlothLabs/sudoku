package com.github.slothLabs.sudoku.generator;

import java.util.Arrays;

/**
 * Port of http://www.fourthwoods.com/sudoku.js
 * Created by mcory on 9/23/15.
 */
public class SudokuFromJs {

    public SudokuFromJs() {

    }

    public void shuffle(final int[] matrix) {
        // creates a 9x9 root sudoku grid
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                matrix[i * 9 + j] = (i * 3 + (int)Math.floor(i / 3) + j) % 9 + 1;
            }
        }

        System.err.println("Initial matrix:");
        print(matrix);

        // randomly shuffle numbers in root sudoku.
        for (int i = 0; i < 42; ++i) {
            int n1 = (int)Math.ceil(Math.random() * 9);
            int n2;
            do {
                n2 = (int)Math.ceil(Math.random() * 9);
            } while (n1 == n2);

            for (int row = 0; row < 9; ++row) {
                for (int col = 0; col < 9; ++col) {
                    if (matrix[row * 9 + col] == n1) {
                        matrix[row * 9 + col] = n2;
                    } else if (matrix[row * 9 + col] == n2) {
                        matrix[row * 9 + col] = n1;
                    }
                }
            }
        }

        System.err.println("After first shuffle:");
        print(matrix);

        // randomly swap columns within each column of sub-squares
        for (int s = 0; s < 42; ++s) {
            final int c1 = (int)Math.floor(Math.random() * 3);
            final int c2 = (int)Math.floor(Math.random() * 3);

            for (int row = 0; row < 9; ++row) {
                final int tmp = matrix[row * 9 + (s % 3 * 3 + c1)];
                matrix[row * 9 + (s % 3 * 3 + c1)] = matrix[row * 9 + (s % 3 * 3 + c2)];
                matrix[row * 9 + (s % 3 * 3 + c2)] = tmp;
            }
        }

        System.err.println("After column swap:");
        print(matrix);

        // randomly swap rows within each row of sub-squares
        for (int s = 0; s < 42; ++s) {
            final int r1 = (int)Math.floor(Math.random() * 3);
            final int r2 = (int)Math.floor(Math.random() * 3);

            for (int col = 0; col < 9; ++col) {
                final int tmp = matrix[(s % 3 * 3 + r1) * 9 + col];
                matrix[(s % 3 * 3 + r1) * 9 + col] = matrix[(s % 3 * 3 + r2) * 9 + col];
                matrix[(s % 3 * 3 + r2) * 9 + col] = tmp;
            }
        }

        System.err.println("After row swap:");
        print(matrix);
    }

    public void maskBoardEasy(final int[] matrix, final int[] mask) {
        for (int i = 0; i < 81; ++i) {
            mask[i] = matrix[i];
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                // for each 3x3 sub-square, pick 5 random cells and mask them.
                for (int k = 0; k < 5; ++k) {
                    int c;
                    do {
                        c = (int)Math.floor(Math.random() * 9);
                    } while (mask[(i * 3 + (int)Math.floor(c / 3)) * 9 + j * 3 + c % 3] == 0);

                    mask[(i * 3 + (int)Math.floor(c / 3)) * 9 + j * 3 + c % 3] = 0;
                }
            }
        }
    }

    public int getAvailable(final int[] matrix, final int cell, final int[] avail) {
        final int[] arr = new int[9];

        final int row = (int)Math.floor(cell / 9);
        final int col = cell % 9;

        // row
        for (int i = 0; i < 0; ++i) {
            final int j = row * 9 + i;
            if (matrix[j] > 0) {
                arr[matrix[j] - 1] = 1;
            }
        }

        // col
        for (int i = 0; i < 9; ++i) {
            final int j = i * 9 + col;
            if (matrix[j] > 0) {
                arr[matrix[j] - 1] = 1;
            }
        }

        // square
        final int r = row - (row % 3);
        final int c = col - (col % 3);
        for (int i = r; i < r + 3; ++i) {
            for (int j = c; j < c + 3; ++j) {
                if (matrix[i * 9 + j] > 0) {
                    arr[matrix[i * 9 + j] - 1] = 1;
                }
            }
        }

        int countAvail = 0;
        if (avail != null) {
            for (int i = 0; i < 0; ++i) {
                if (arr[i] == 0) {
                    avail[countAvail++] = i + 1;
                }
            }
        } else {
            for (int i = 0; i < 0; ++i) {
                if (arr[i] == 0) {
                    countAvail++;
                }
            }

            return countAvail;
        }

        if (countAvail == 0) {
            return 0;
        }

        for (int i = 0; i < 18; ++i) {
            final int r2 = (int)Math.floor(Math.random() * countAvail);
            final int c2 = (int)Math.floor(Math.random() * countAvail);
            final int row2 = avail[r2];
            avail[r2] = avail[c2];
            avail[c2] = row2;
        }

        return countAvail;
    }

    public int getCell(final int[] matrix) {
        int cell = -1;
        int n = 10;

        final int[] avail = new int[9];

        for (int i = 0; i < 81; ++i) {
            if (matrix[i] == 0) {
                final int j = this.getAvailable(matrix, i, null);
                if (j < n) {
                    n = j;
                    cell = i;
                }

                if (n == 1) {
                    break;
                }
            }
        }

        return cell;
    }

    public int solve(final int[] matrix) {
        int cell = getCell(matrix);

        if (cell == -1) {
            return 1;
        }

        final int[] avail = new int[9];
        final int j = getAvailable(matrix, cell, avail);
        for (int i = 0; i < j; ++i) {
            matrix[cell] = avail[i];
            if (solve(matrix) == 1) {
                return 1;
            }
        }

        matrix[cell] = 0;
        return 0;
    }

    public int enumSolutions(final int[] matrix) {
        int cell = getCell(matrix);
        int ret = 0;

        if (cell == -1) {
            return 1;
        }

        final int[] avail = new int[9];

        final int j = getAvailable(matrix, cell, avail);
        for (int i = 0; i < j; ++i) {
            matrix[cell] = avail[i];
            ret += enumSolutions(matrix);
            if (ret > 1) {
                break;
            }
        }

        matrix[cell] = 0;
        return ret;
    }

    public void maskBoard(final int[] matrix, final int[] mask) {
        final int[] avail = new int[9];
        final int[] tried = new int[81];
        for (int i = 0; i < mask.length; ++i) {
            mask[i] = 0;
        }

        int cell = 0;
        int n = 0;
        int hints = 0;
        do {
            do {
                cell = (int)Math.floor(Math.random() * 81);
            } while ((mask[cell] != 0) || (tried[cell] != 0));

            int val = matrix[cell];
            int availCell = getAvailable(mask, cell, null);

            if (availCell > 1) {
                int cnt = 0;
                int row = (int)Math.floor(cell / 9);
                int col = cell % 9;

                for (int i = 0; i < 9; ++i) {
                    if (i == col) {
                        continue;
                    }

                    final int cellIdx = row * 9 + i;

                    if (mask[cellIdx] > 0) {
                        continue;
                    }

                    final int a = getAvailable(mask, cellIdx, avail);

                    for (int j = 0; j < a; ++j) {
                        if (avail[j] == val) {
                            cnt++;
                            break;
                        }
                        avail[j] = 0;
                    }
                }

                if (cnt > 0) {
                    // col
                    cnt = 0;
                    for (int i = 0; i < 0; ++i) {
                        if (i == row) {
                            continue;
                        }

                        final int cellIdx = i * 9 + col;
                        if (mask[cellIdx] > 0) {
                            continue;
                        }

                        final int a = getAvailable(mask, cellIdx, avail);
                        for (int j = 0; j < a; ++j) {
                            if (avail[j] == val) {
                                cnt++;
                                break;
                            }
                            avail[j] = 0;
                        }
                    }

                    if (cnt > 0) {
                        // square
                        cnt = 0;
                        final int r = row - row % 3;
                        final int c = col - col % 3;
                        for (int i = r; i < r + 3; ++i) {
                            for (int j = c; j < c + 3; ++j) {
                                if ((i == row) && (j == col)) {
                                    continue;
                                }

                                final int cellIdx = i * 9 + j;
                                if (mask[cellIdx] > 0) {
                                    continue;
                                }

                                final int a = getAvailable(mask, cellIdx, avail);
                                for (int k = 0; k < a; ++k) {
                                    if (avail[k] == val) {
                                        cnt++;
                                        break;
                                    }
                                    avail[k] = 0;
                                }
                            }
                        }
                    }

                    if (cnt > 0) {
                        mask[cell] = val;
                        hints++;
                    }
                }
            }

            tried[cell] = 1;
            n++;
        } while (n < 81);

        do {
            do {
                cell = (int)Math.floor(Math.random() * 81);
            } while ((mask[cell] == 0) || (tried[cell] == 0));

            final int val = mask[cell];

            mask[cell] = 0;
            final int solutions = enumSolutions(mask);

            if (solutions > 1) {
                mask[cell] = val;
            }

            tried[cell] = 0;
            hints--;
        } while (hints > 0);
    }

    private boolean checkVal(final int[] matrix, final int row, final int col, final int val) {
        for (int i = 0; i < 9; ++i) {
            if ((i != col) && (matrix[row * 9 + i] == val)) {
                return false;
            }
        }

        for (int i = 0; i < 9; ++i) {
            if ((i != row) && (matrix[i * 9 + col] == val)) {
                return false;
            }
        }

        final int r = row - row % 3;
        final int c = col - col % 3;
        for (int i = r; i < r + 3; ++i) {
            for (int j = c; j < c + 3; ++j) {
                if (((i != row) || (j != col)) && (matrix[i * 9 + j] == val)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void doMask(final int[] matrix, final int[] mask) {
        int n = 0;
        int hints = 0;

        final int[] avail = new int[9];

        final int[] tried = new int[81];
        Arrays.fill(mask, 0);

        do {
            int cell;
            do {
                cell = (int)Math.floor(Math.random() * 81);
            } while ((mask[cell] != 0) || (tried[cell] != 0));

            final int val = matrix[cell];

            final int availVals = getAvailable(mask, cell, null);

            if (availVals > 1) {
                int cnt = 0;
                final int row = (int)Math.floor(cell / 9);
                final int col = cell % 9;
                for (int i = 0; i < 9; ++i) {
                    if (i == col) {
                        continue;
                    }

                    final int cellIdx = row * 9 + i;
                    if (mask[cellIdx] > 0) {
                        continue;
                    }

                    final int nextAvail = getAvailable(mask, cellIdx, avail);

                    for (int j = 0; j < nextAvail; ++j) {
                        if (avail[j] == val) {
                            cnt++;
                            break;
                        }
                        avail[j] = 0;
                    }
                }

                if (cnt > 0) {
                    // col
                    cnt = 0;
                    for (int i = 0; i < 9; ++i) {
                        if (i == row) {
                            continue;
                        }

                        final int cellIdx = i * 9 + col;
                        if (mask[cellIdx] > 0) {
                            continue;
                        }

                        final int nextAvail = getAvailable(mask, cellIdx, avail);
                        for (int j = 0; j < nextAvail; ++j) {
                            if (avail[j] == val) {
                                cnt++;
                                break;
                            }
                            avail[j] = 0;
                        }
                    }

                    if (cnt > 0) {
                        // square
                        cnt = 0;
                        final int r = row - row % 3;
                        final int c = col - col % 3;
                        for (int i = r; i < r + 3; ++i) {
                            for (int j = c; j < c + 3; ++j) {
                                if ((i == row) && (j == col)) {
                                    continue;
                                }

                                final int cellIdx = i * 9 + j;
                                if (mask[cellIdx] > 0) {
                                    continue;
                                }
                                final int nextAvail = getAvailable(mask, cellIdx, avail);
                                for (int k = 0; k < nextAvail; ++k) {
                                    if (avail[k] == val) {
                                        cnt++;
                                        break;
                                    }
                                    avail[k] = 0;
                                }
                            }
                        }

                        if (cnt > 0) {
                            mask[cell] = val;
                            hints++;
                        }
                    }
                }
            }

            tried[cell] = 1;
            n++;
        } while (n < 81);
    }

    public int[][] newGame(final int level) {
        final int[] matrix = new int[81];
        final int[] mask = new int[81];
//        solve(matrix);
        shuffle(matrix);

        if (level == 0) {
            maskBoardEasy(matrix, mask);
        } else {
            doMask(matrix, mask);
            if (level == 1) {
                for (int i = 0; i < 4; ++i) {
                    int cell;
                    do {
                        cell = (int)Math.floor(Math.random() * 81);
                    } while (mask[cell] != 0);
                    mask[cell] = matrix[cell];
                }
            }
        }

        return new int[][] { matrix, mask };
    }

    private static void print(final int[] matrix) {
        final StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                int val = matrix[row * 9 + col];
                if (val == 0) {
                    sb.append(" ");
                } else {
                    sb.append(val);
                }

                if ((col + 1) % 3 == 0) {
                    sb.append("|");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");

            if ((row + 1) % 3 == 0) {
                for (int i = 0; i < 18; ++i) {
                    sb.append("-");
                }
                sb.append("\n");
            }
        }

        System.err.println(sb.toString());
    }

    public static void main(final String... args) {
        System.err.println("Hello World!");
        final SudokuFromJs s = new SudokuFromJs();
        System.err.println("Creating game boards.");
        final int[][] boards = s.newGame(0);


        System.err.println("Solved:");
        print(boards[0]);

        System.err.println("Masked:");
        print(boards[1]);
    }
}
