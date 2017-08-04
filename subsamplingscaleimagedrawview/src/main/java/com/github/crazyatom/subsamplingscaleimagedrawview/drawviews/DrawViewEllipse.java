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
 * Created by crazy on 2017-07-11.
 */

public class DrawViewEllipse extends BaseDrawView {

    public DrawViewEllipse(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.ELLIPSE, imageDrawView);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "E" : getResources().getString(R.string.ellipse);
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
        canvas.drawOval(vRect, paint);
        super.onDraw(canvas);
    }

    /**
     * 페인트 설정
     *
     */
    @Override
    protected void loadPaint() {
        super.loadPaint();
        paint.setStyle(Paint.Style.STROKE);
    }
}
