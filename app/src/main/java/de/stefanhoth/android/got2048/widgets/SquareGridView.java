package de.stefanhoth.android.got2048.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.concurrent.atomic.AtomicInteger;

import de.stefanhoth.android.got2048.R;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 30.03.2014 18:43
 * @since TODO add version
 */
public class SquareGridView extends GridView {

    private static final int DEFAULT_GRID_SIZE = 4;
    private static final String TAG = SquareGridView.class.getName();

    private int mGridSize = -1;

    Context mContext;
    private SquareCellView[][] cells;

    public SquareGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initAttributes(context, attrs);
        setupView(context);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray a = null;
        try {
            a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.SquareGridView,
                    0, 0);

            mGridSize = a.getInteger(R.styleable.SquareGridView_gridsize, DEFAULT_GRID_SIZE);
        } catch (NullPointerException e) {
            Log.e(TAG, "Could not read attributes from theme.", e);
        } finally {
            if (a != null) {
                a.recycle();
            }

            if (mGridSize == -1) {
                mGridSize = DEFAULT_GRID_SIZE;
            }
        }
    }

    public int getmGridSize() {
        return mGridSize;
    }

    public void setmGridSize(int mGridSize) {
        this.mGridSize = mGridSize;
        invalidate();
        requestLayout();
    }

    private void setupView(Context context) {

        cells = new SquareCellView[mGridSize][mGridSize];
        this.setNumColumns(mGridSize);

        SquareCellView cell;

        for (int row = 0; row < mGridSize; row++) {
            for (int column = 0; column < mGridSize; column++) {

                cell = new SquareCellView(context);
                cell.setId(generateViewId());

                cell.setBackgroundColor(getResources().getColor(R.color.cell_background));
                cell.setGravity(Gravity.CENTER);
                cells[row][column] = cell;
            }
        }

        this.setAdapter(new CellAdapter(context, 0));

        Log.d(TAG, "setupView: Finished generating " + getChildCount() + " cells.");
    }

    public void updateGrid(int[][] values) {

        for (int row = 0; row < mGridSize; row++) {
            for (int column = 0; column < mGridSize; column++) {

                if (row >= values.length || column >= values[row].length) {
                    Log.w(TAG, "updateGrid: values array is too small. No value to update cell[" + row + "," + column + "]");
                    continue;
                }

                //TODO filter invalid values
                cells[row][column].setText(String.valueOf(values[row][column]));
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int lower = Math.min(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(lower, lower);
    }

    /**
     * Listener exposing events for animations in the grid
     */
    public interface AnimationListener {

        public void onAnimationStart();

        public void onAnimationEnded();
    }


    class CellAdapter extends ArrayAdapter<SquareCellView> {

        private final String TAG = CellAdapter.class.getName();

        public CellAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public SquareCellView getItem(int position) {
            return cells[linearToGridRow(position)][linearToGridColumn(position)];
        }

        @Override
        public long getItemId(int position) {
            return cells[linearToGridRow(position)][linearToGridColumn(position)].getId();
        }

        @Override
        public int getCount() {
            return mGridSize * mGridSize;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int row = linearToGridRow(position);
            int column = linearToGridColumn(position);

            Log.d(TAG, "getView: Retrieving view for position=" + position + " => cell [" + row + "," + column + "]");
            return cells[row][column];
        }

        private int linearToGridRow(int position) {
            int row = (int) Math.floor(position / mGridSize);
            //Log.d(TAG, "linearToGridRow: position="+position+" => row="+row);
            return row;
        }

        private int linearToGridColumn(int position) {
            int column = position % mGridSize;
            //Log.d(TAG, "linearToGridColumn: position="+position+" => column="+column);
            return column;
        }
    }


    //taken from http://stackoverflow.com/a/15442898/409349
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
