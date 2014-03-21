package de.stefanhoth.android.got2048.logic.model;

/**
 * Represents one cell on the playing field
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:25
 * @since 0.1
 */
public class Cell {

    private Integer value;
    private int row;
    private int column;
    private boolean immune;

    public Cell() {
        this.value = null;
        row = -1;
        column = -1;
    }

    public Cell(int row, int column) {
        this.value = null;
        this.row = row;
        this.column = column;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public boolean hasValue(){
        return value != null;
    }

    public void emptyField(){
        this.value = null;
    }

    protected int getRow() {
        return row;
    }

    protected void setRow(int row) {
        this.row = row;
    }

    protected int getColumn() {
        return column;
    }

    protected void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (column != cell.column) return false;
        if (row != cell.row) return false;
        if (value != null ? !value.equals(cell.value) : cell.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + row;
        result = 31 * result + column;
        return result;
    }

    public boolean isImmune() {
        return immune;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
    }
}
