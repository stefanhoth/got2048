package de.stefanhoth.android.got2048.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import de.stefanhoth.android.got2048.R;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:00
 * @since TODO add version
 */
public class SquareCellView extends TextView {

    private int mValue = -1;

    public SquareCellView(Context context) {
        super(context);
        init();
    }

    public SquareCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SquareCellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.cell_font_size));
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setValue(int value) {
        mValue = value;

        if (mValue == -1) {
            this.setText("");
            this.setBackgroundColor(getResources().getColor(R.color.cell_background_default));
        } else {
            this.setText(String.valueOf(mValue));

            int colorValue = R.color.cell_background_step_1;

            if (mValue <= 2) {
                colorValue = R.color.cell_background_step_1;
            } else if (mValue <= 4) {
                colorValue = R.color.cell_background_step_2;
            } else if (mValue <= 8) {
                colorValue = R.color.cell_background_step_3;
            } else if (mValue <= 16) {
                colorValue = R.color.cell_background_step_4;
            } else if (mValue <= 32) {
                colorValue = R.color.cell_background_step_5;
            } else if (mValue <= 64) {
                colorValue = R.color.cell_background_step_6;
            } else if (mValue <= 128) {
                colorValue = R.color.cell_background_step_7;
            } else if (mValue <= 256) {
                colorValue = R.color.cell_background_step_8;
            } else if (mValue <= 512) {
                colorValue = R.color.cell_background_step_9;
            } else if (mValue <= 1024) {
                colorValue = R.color.cell_background_step_10;
            } else if (mValue <= 2048) {
                colorValue = R.color.cell_background_step_11;
            } else if (mValue > 2048) {
                colorValue = R.color.cell_background_step_1;
            }

            this.setBackgroundColor(getResources().getColor(colorValue));
        }
    }

}
