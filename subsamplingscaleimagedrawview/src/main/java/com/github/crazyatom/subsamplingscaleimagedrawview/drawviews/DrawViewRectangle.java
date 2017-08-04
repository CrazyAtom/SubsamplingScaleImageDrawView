package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewRectangle extends BaseDrawView {

    public DrawViewRectangle(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.RECTANGLE, imageDrawView);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "R" : getResources().getString(R.string.rectangle);
    }

    @Override
    protected void preCalc() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        if (sRegion == null) {
            return;
        }

        RectF vRect = new RectF();
        imageDrawView.sourceToViewRect(sRegion, vRect);
        if (vRect.width() == 0 || vRect.height() == 0) {
            return;
        }

        loadPaint();

        canvas.drawRect(vRect, paint);

        super.onDraw(canvas);
    }

    /**
     * 페인트 설정
     *
     */
    private void loadPaint() {
        paint.setColor(Color.parseColor(color));
        paint.setStrokeWidth(thick);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }
}
