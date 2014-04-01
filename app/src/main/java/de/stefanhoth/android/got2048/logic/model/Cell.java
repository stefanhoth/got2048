package de.stefanhoth.android.got2048.logic.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents one cell on the playing field
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:25
 * @since 0.1
 */
public class Cell implements Parcelable {

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
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.row);
        dest.writeInt(this.column);
    }

    private Cell(Parcel in) {
        this.row = in.readInt();
        this.column = in.readInt();
    }

    public static Parcelable.Creator<Cell> CREATOR = new Parcelable.Creator<Cell>() {
        public Cell createFromParcel(Parcel source) {
            return new Cell(source);
        }

        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };
}
