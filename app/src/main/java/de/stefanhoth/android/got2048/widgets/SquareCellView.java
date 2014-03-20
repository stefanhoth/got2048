package de.stefanhoth.android.got2048.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:00
 * @since TODO add version
 */
public class SquareCellView extends LinearLayout {
    public SquareCellView(Context context) {
        super(context);
    }

    public SquareCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
