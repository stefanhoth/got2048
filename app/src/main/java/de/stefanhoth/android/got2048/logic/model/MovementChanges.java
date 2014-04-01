package de.stefanhoth.android.got2048.logic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Container to carry over changes a movement caused to the playing field
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 01.04.2014 12:49
 * @since TODO add version
 */
public class MovementChanges implements Parcelable {

    public boolean cellsMoved;
    public int pointsEarned;
    public int[][] gridStatus;
    private List<Cell> addedCells;
    public int addedCellsValue;

    public MovementChanges(int[][] gridStatus) {
        cellsMoved = false;
        pointsEarned = 0;
        this.gridStatus = gridStatus;
        this.addedCells = new ArrayList<>();
        this.addedCellsValue = 2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(cellsMoved ? (byte) 1 : (byte) 0);
        dest.writeInt(this.pointsEarned);
        dest.writeSerializable(this.gridStatus);
        dest.writeInt(this.addedCellsValue);
        dest.writeParcelableArray(addedCells.toArray(new Cell[addedCells.size()]), 0);
    }

    private MovementChanges(Parcel in) {
        this.cellsMoved = in.readByte() != 0;
        this.pointsEarned = in.readInt();
        this.gridStatus = (int[][]) in.readSerializable();
        this.addedCellsValue = in.readInt();
        this.addedCells = Arrays.asList((Cell[]) in.readParcelableArray(Cell.class.getClassLoader()));
    }

    public static Parcelable.Creator<MovementChanges> CREATOR = new Parcelable.Creator<MovementChanges>() {
        public MovementChanges createFromParcel(Parcel source) {
            return new MovementChanges(source);
        }

        public MovementChanges[] newArray(int size) {
            return new MovementChanges[size];
        }
    };

    public void addCell(Cell cell, int startValue) {

        addedCells.add(cell);
        this.addedCellsValue = startValue;
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }
}
