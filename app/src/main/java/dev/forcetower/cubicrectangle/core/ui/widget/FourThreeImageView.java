package dev.forcetower.cubicrectangle.core.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.getSize;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * A extension of ForegroundImageView that is always 4:3 aspect ratio.
 */
public class FourThreeImageView extends ForegroundImageView {

    public FourThreeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int fourThreeHeight = makeMeasureSpec(getSize(widthSpec) * 14 / 9, EXACTLY);
        super.onMeasure(widthSpec, fourThreeHeight);
    }
}