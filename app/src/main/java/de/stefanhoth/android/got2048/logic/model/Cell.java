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

    public Cell() {
        this.value = null;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public boolean hasValue(){
        return value == null;
    }

    public void emptyField(){
        this.value = null;
    }
}
