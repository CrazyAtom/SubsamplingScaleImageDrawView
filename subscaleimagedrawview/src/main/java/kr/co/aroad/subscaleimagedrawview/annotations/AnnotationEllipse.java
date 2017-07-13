package kr.co.aroad.subscaleimagedrawview.annotations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class AnnotationEllipse extends BaseAnnotation {

    public AnnotationEllipse(Context context, ImageDrawView imageDrawView) {
        super(context, AnnotationType.ELLIPSE, imageDrawView);
    }

    @Override
    public void updateAnnotEx(RectF newBbox) {
        ArrayList<PointF> points = new ArrayList<>();
        points.add(new PointF(newBbox.left, newBbox.top));
        points.add(new PointF(newBbox.right, newBbox.bottom));
        updateAnnotEx(points);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public boolean isInvalidSaveAnnotEx() {
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 2) {
            loadPaint();
            setBbox();

            final RectF rect = getRect(true);
            canvas.drawOval(rect, mPaint);

            super.onDraw(canvas);
        }
    }

    protected void setBbox() {
        final RectF rect = getRect(false);
        setBbox(rect);
    }

    /**
     * 페인트 설정
     *
     */
    private void loadPaint() {
        mPaint.setColor(Color.parseColor(mColor));
        mPaint.setStrokeWidth(mThick);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }
}
