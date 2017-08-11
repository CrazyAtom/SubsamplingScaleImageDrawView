package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseEditPinView;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.EditPinViewRect;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class DrawViewInk extends BaseDrawView {

    public DrawViewInk(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.INK, imageDrawView);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "I" : getResources().getString(R.string.ink);
    }

    @Override
    protected void preCalc() {

    }

    @Override
    public BaseEditPinView getEditPinView() {
        return new EditPinViewRect(imageDrawView, this);
    }

    @Override
    public boolean isValidEditedFlag() {
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (sRegion == null) {
            return;
        }

        loadPaint();

        Path path = new Path();
        PointF vPrev = sourceToViewCoord(getPosition(0).x, getPosition(0).y);
        path.moveTo(vPrev.x, vPrev.y);
        for (int i = 1; i < getPositionSize(); i++) {
            PointF vPoint = sourceToViewCoord(getPosition(i).x, getPosition(i).y);
            path.quadTo(vPrev.x, vPrev.y, (vPoint.x + vPrev.x) / 2, (vPoint.y + vPrev.y) / 2);
            vPrev = vPoint;
        }
        canvas.drawPath(path, this.paint);

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
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }
}
