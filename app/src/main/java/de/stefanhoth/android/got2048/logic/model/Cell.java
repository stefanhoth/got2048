package de.stefanhoth.android.got2048.logic.model;

/**
 * Represents one cell on the playing field
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:25
 * @since 0.1
 */
public class Cell {

    private int row;
    private int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (column != cell.column) return false;
        if (row != cell.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + row;
        result = 31 * result + column;
        return result;
    }
}
